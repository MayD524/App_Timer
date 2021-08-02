#!/usr/bin/env python3
"""
    Name: App Timer
    Authors: Cross, Sweden
    Date: 8/1/2021
    Description: Gets a counter for all currently running apps
"""

import psutil
import time
import UPL
import os

__version__ = "0.1.1"


def get_procs() -> list:
    """
        Gets all currently running processes
    
        @params:
            None
            
        @return:
            procs  (list) : all currently running processes
    """
    tmp =[process for process in psutil.process_iter()]
    procs= []
    name = os.getlogin().lower()
    
    for proc in tmp:
        try:
            if name in proc.username().lower():
                procs.append(proc.name().lower().replace(".exe", ''))
        
        except Exception:
            pass
        
    ignore = UPL.Core.file_manager.getData_json("ignore.json")
    procs = list(set([i for i in procs if i not in ignore[os.name]]))

    return procs

def main(config:dict) -> None:
    """
        the main function
        @params:
            config (dict) : all config options
            
        @returns:
            None
    
    """
    ## all apps
    apps = UPL.Core.file_manager.getData_json("apps.json")
    current_iter = 0
    
    ## main loop
    while True:
        ## get all processes every tick
        start = time.time()
        processes = get_procs()
        added_time = round(abs(start-time.time()))  
        for app in processes:
            if app not in apps.keys():
                apps[app] = added_time
                
            else:
                apps[app] += config["time_between"] + added_time
        
        ## time shit
        if current_iter == config["log_update"]:
            current_iter = 0
            # update json
            UPL.Core.file_manager.write_json("apps.json", apps, 2)
            apps = UPL.Core.file_manager.getData_json("apps.json")
        
        ## update ever second
        current_iter += config["time_between"]
        time.sleep(config["time_between"])

if __name__ == "__main__":
    main(UPL.Core.file_manager.getData_json("config.json"))
    print(chr(69)) # if you see capital E you are fucked
    
else:
    raise ImportError("This cannot be imported")