#!/usr/bin/env python3
"""
    Name: App Timer Cli
    Authors: Cross
    Date: 8/1/2021
    Description: A command line option for interacting with the timer
    version: 0.1.0b
"""

import subprocess
import datetime
import time
import UPL
import sys

__version__ = "0.1.0b"

class app_timer_cli:
    def __init__(self, mode:int) -> None:
        self.mode = mode
        self.config = UPL.Core.file_manager.getData_json("config.json")
        
        ## start if not already running
        #if self.config['running'] == False:
        #    subprocess.Popen("py main.py")
            
        if self.mode == 0:
            self.userInteractable()
        else:
            self.constant()
    
    def timeFormat(self, secs:int) -> str:
        conv = datetime.timedelta(seconds=secs)
        return str(conv)
    
    def constant(self) -> None:
        while True:
            UPL.Core.clear() ## clear the terminal
            apps = UPL.Core.file_manager.getData_json("apps.json")
            print("App Name : Up time")
            
            for app in apps.keys():
                print(f"{app} : {self.timeFormat(apps[app])}")
            
            ## log updates log_update + 1
            time.sleep(self.config["log_update"] + 1)
            
    def display_help(self) -> None:
        print("equ:[name]\nends:[text]\nstarts:[text]\ncontains:[text]\nequt:[time (seconds)]\nless:[time]\ngreat:[time]\n:all -> displays all\n:h -> help\n:c -> Clear screen\n:q -> Quit")

    def run_cmd(self, text:str, macro=False) -> None:
        apps = UPL.Core.file_manager.getData_json("apps.json")
        if text == ":q":
            sys.exit(0)
            
        elif text == ":h":
            self.display_help()
            return
        
        ## clear screen
        elif text == ":c":
            UPL.Core.clear()
            return 
        
        elif text == ":all":
            print("App name : App time")    
            for app in apps.keys():
                print(f"{app} : {self.timeFormat(apps[app])}")
            return
        try:
            cmd, txt = text.split(":")
            cmd = cmd.lower()
        
        except Exception as e:
            print("There was an issue with that command")
            print(e)
            print(chr(69))
            return # just to not cause issues later
        
        if cmd == 'equ':
            apps = {x:i for x, i in apps.items() if x == txt}
        
        elif cmd == 'starts':
            apps = {x:i for x, i in apps.items() if x.startswith(txt)}
        
        elif cmd == 'ends':
            apps = {x:i for x, i in apps.items() if x.endswith(txt)}
            
        elif cmd == 'contains':
            apps = {x:i for x, i in apps.items() if txt in x}
            
        elif cmd == "equt":
            apps = {x:i for x, i in apps.items() if i == int(txt)}
        
        elif cmd == "less":
            apps = {x:i for x, i in apps.items() if i <= int(txt)}
        
        elif cmd == "great":
            apps = {x:i for x, i in apps.items() if i >= int(txt)}
            
        print("App name : App time")    
        for app in apps.keys():
            print(f"{app} : {self.timeFormat(apps[app])}")

    def userInteractable(self) -> None:
        self.display_help()
        while True:
            inp = UPL.Core.ainput("> ", str) 
            self.run_cmd(inp)
            
def start():
    print("Modes:\n[0] Interactable [1] Non-Interactable")
    mode = UPL.Core.ainput("Mode> ", int)
    if mode > 1 or mode < 0:
        mode = 0
    cli = app_timer_cli(mode)

## script start
if __name__ == "__main__":
    start()