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
        print("equ:[name]\nends:[text]\nstarts:[text]\ncontains:[text]\nmacro:[macro name]\n:all -> displays all\n:q -> Quit")

    
    def userInteractable(self) -> None:
        while True:
            inp = UPL.Core.ainput("> ", str)
            
            if inp == ":help": 
                self.display_help() 
                continue
            
            if inp == ":q":
                sys.exit(0)
            
            cmd, text = inp.split(':',1)
            
            if cmd == "equ":
                pass
            
            elif cmd == "ends":
                pass
            
            elif cmd == "starts":
                pass
            
            elif cmd == "contains":
                pass
            
            elif cmd == "macro":
                pass
            
            
            pass
            
            
    
        

def start():
    print("Modes:\n[0] Interactable [1] Non-Interactable")
    mode = UPL.Core.ainput("Mode> ", int)
    if mode > 1 or mode < 0:
        mode = 0
    cli = app_timer_cli(mode)

## script start
if __name__ == "__main__":
    start()