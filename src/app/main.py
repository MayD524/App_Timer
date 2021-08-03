#!/usr/bin/env python3
"""
    Name: App Timer
    Authors: Cross, Sweden
    Date: 8/1/2021
    Description: Gets a counter for all currently running apps
"""
from datetime import datetime
import psutil
import time
import UPL
import os

__version__ = "0.1.2"

class app_timer:
    def __init__(self, config:dict) -> None:
        self.config = config
        self.reced_apps = []
        self.apps = UPL.Core.file_manager.getData_json("apps.json")
        self.prev = ""
    
    def get_time(self, pid:int, current:str, app_name:str) -> None:
        current = current.split(":")
        if app_name not in self.reced_apps:
            app_time = time.strftime("%H:%M:%S", time.localtime(psutil.Process(pid).create_time())).split(":")
            hours = int(current[0]) - int(app_time[0])
            mins = (int(current[1]) - int(app_time[1])) + (hours * 60)
            secs = (int(current[2]) - int(app_time[2])) + (mins * 60)
            if app_name not in self.apps.keys():
                self.apps[app_name] = secs
            else:
                self.apps[app_name] += secs
            self.reced_apps.append(app_name)
            return
        
        if self.prev == "":
            app_time = time.strftime("%H:%M:%S", time.localtime(psutil.Process(pid))).split(":")
            
            hours = int(current[0]) - int(app_time[0])
            mins = (int(current[1]) - int(app_time[1])) + (hours * 60)
            secs = (int(current[2]) - int(app_time[2])) + (mins * 60)
        
        else:
            prev = self.prev.split(":")
            hours = int(current[0]) - int(prev[0])
            mins = (int(current[1]) - int(prev[1])) + (hours * 60)
            secs = (int(current[2]) - int(prev[2])) + (mins * 60)
            
        self.apps[app_name] += secs
    
    def get_procs(self) -> None:
        ignore = UPL.Core.file_manager.getData_json("ignore.json")
        name = os.getlogin().lower()
        current = datetime.now().strftime("%H:%M:%S")
        self.apps = UPL.Core.file_manager.getData_json("apps.json")
        for proc in psutil.process_iter():
            try:
                if name in proc.username().lower() and proc.name().lower().replace(".exe", '') not in ignore:
                    self.get_time(proc.pid, current, proc.name().lower().replace(".exe", ''))
            except Exception:
                pass
        
        self.prev = current
    
    def main(self) -> None:
        while True:
            self.get_procs()
            UPL.Core.file_manager.write_json("apps.json", self.apps, 2)
            time.sleep(self.config["log_update"])
            

if __name__ == "__main__":
    #main(UPL.Core.file_manager.getData_json("config.json"))
    app_main = app_timer(UPL.Core.file_manager.getData_json('config.json'))
    app_main.main()
    print(chr(69)) ## debug E
    
else:
    raise ImportError("This cannot be improted")