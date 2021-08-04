#!/usr/bin/env python3
"""
    Name: App Timer
    Authors: Cross, Sweden
    Date: 8/1/2021
    Description: Gets a counter for all currently running apps
    version: 0.1.5b
"""
from datetime import datetime
import psutil
import time
import UPL
import os

__version__ = "0.1.4"

def get_seconds(current:list, app_time:list) -> int:
    ## convert to seconds
    hours = int(current[0]) - int(app_time[0])
    mins = (int(current[1]) - int(app_time[1])) + (hours * 60)
    return (int(current[2]) - int(app_time[2])) + (mins * 60)

class app_timer:
    def __init__(self, config:dict) -> None:
        self.config = config
        self.config_update = 0
        self.reced_apps = []
        self.apps = UPL.Core.file_manager.getData_json("apps.json")
        self.prev = self.config['last_known_time'] if self.config['last_known_time'] != "" else datetime.now().strftime("%H:%M:%S")
    
        ## if apps.json we treat that as a fresh boot
        if self.apps == {}:
            self.config["first_boot"] = True
    
    def get_time(self, pid:int, current:str, app_name:str, first_boot:bool) -> None:
        current = current.split(":") ## get current time
        if app_name not in self.reced_apps:
            if first_boot:   
                app_time = time.strftime("%H:%M:%S", time.localtime(psutil.Process(pid).create_time())).split(":")
                secs = get_seconds(current, app_time)
            
            else:
                secs = get_seconds(current, self.prev.split(":"))    
            
            ## add to apps.json
            if app_name not in self.apps.keys():
                self.apps[app_name] = secs
            ## update it
            else:
                self.apps[app_name] += secs
            
            ## quick check to see if it's a new app or not
            self.reced_apps.append(app_name)
            return
        
        ## get the seconds based on the previously known time
        secs = get_seconds(current, self.prev.split(":"))
        
        ## adding to the key
        if app_name in self.apps.keys():
            self.apps[app_name] += secs
        
        ## adding to self.apps
        else:
            self.apps[app_name] = secs

    def get_procs(self, first_boot:bool) -> None:
        ## get all processes
        
        ## ignore these processes
        ignore = UPL.Core.file_manager.getData_json("ignore.json")
        ## get current user
        name = os.getlogin().lower()
        ## get current time
        current = datetime.now().strftime("%H:%M:%S")
        ## update the self.apps to the most up to date version
        self.apps = UPL.Core.file_manager.getData_json("apps.json")
        
        done = []
        ## get all current processes
        for proc in psutil.process_iter():
            if proc.name().lower().replace(".exe", '') in done:
                continue
            done.append(proc.name().lower().replace(".exe", ''))
            try: ### we can access
                if name in proc.username().lower() and proc.name().lower().replace(".exe", '') not in ignore:
                    ## get the times
                    self.get_time(proc.pid, current, proc.name().lower().replace(".exe", ''), first_boot)
           
           ## something went wrong (if we print it went really wrong)
            except Exception as e: ## we can't access or other generic issue
                ## psutil.AccessDenied is an error that happens
                ## when we attempt to get info from the system
                if type(e) != psutil.AccessDenied:
                    print(e)
                    print(chr(69))

                ## just ignore if it's not any other exception
                pass
        
        self.config["last_known_time"] = current
        self.prev = current
        self.config_update += 1
        
        if self.config_update == self.config["config_update"]:
            self.config_update = 0
            UPL.Core.file_manager.write_json("config.json", self.config, 2)
    
    ## class main func
    def main(self) -> None:
        ## dont run more then oncec
        #if self.config['running']:
        #    raise Exception("Two instances cannot run at the same time")
        
        ## is this the first time we've run the script today? && this boot?
        boot = True
        self.config["running"] = True
        UPL.Core.file_manager.write_json("config.json", self.config, 2)
        if self.config["first_boot"] == False:
            if self.config['first_boot_Time'] != datetime.now().strftime("%Y-%m-%d"):
                self.config["first_boot"] = True
            else:
                boot = False
            
        if self.config["first_boot"]:
            self.config['first_boot'] = False
            self.config["first_boot_Time"] = datetime.now().strftime("%Y-%m-%d") 
            UPL.Core.file_manager.write_json("config.json", self.config, 2)
        
        update_times = 0
        ## main loop
        while True:
            ## get all times and push to self.apps (dict)
            self.get_procs(boot)
            
            update_times += 1
            
            if update_times >= self.config["log_update"]:
                ## update apps.json (for GUI.jar)
                UPL.Core.file_manager.write_json("apps.json", self.apps, 2)

            time.sleep(1)
            

## Script start
if __name__ == "__main__":
    app_main = app_timer(UPL.Core.file_manager.getData_json('config.json'))
    app_main.main()
    print(chr(69)) ## debug E

## dont import me
else:
    raise ImportError("This cannot be improted")