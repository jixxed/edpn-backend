from colorama import Fore as __Fore, Style as __Style
import colorama
import datetime

colorama.init(convert=True)

__bold = '\033[1m'
__normal = "\033[0m"

def Logger(logRecord):
    message = logRecord.msg
    group = logRecord.name
    levelno = logRecord.levelno
    if len(logRecord.args) > 0: message = message % logRecord.args
    if levelno == 20: logInfo(message, group)
    elif levelno == 30: logWarning(message, group)
    elif levelno == 40: logError(message, group)
    elif levelno == 50: logCritical(message, group)

def logDatetime():
    logTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    logTimeString = f"{__Fore.LIGHTBLACK_EX}{__bold}{logTime}{__normal}"
    print(f"{logTimeString}", end=f"{__Style.RESET_ALL} ")

def logInfo(message, group, end="\n"):
    logDatetime()
    groupString = f"{__normal}{__Fore.MAGENTA}{group}{__Style.RESET_ALL}"
    print(f"{__Fore.BLUE}{__Style.BRIGHT}INFO{__Style.RESET_ALL}     {groupString} {__Style.RESET_ALL}{message}", end=end)

def logWarning(message, group, end="\n"):
    logDatetime()
    groupString = f"{__normal}{__Fore.MAGENTA}{group}{__Style.RESET_ALL}"
    print(f"{__Fore.YELLOW}{__Style.BRIGHT}WARNING{__Style.RESET_ALL}  {groupString} {__Style.RESET_ALL}{message}", end=end)

def logError(message, group, end="\n"):
    logDatetime()
    groupString = f"{__normal}{__Fore.MAGENTA}{group}{__Style.RESET_ALL}"
    print(f"{__Fore.RED}{__Style.BRIGHT}ERROR{__Style.RESET_ALL}    {groupString} {__Style.RESET_ALL}{message}", end=end)

def logCritical(message, group, end="\n"):
    logDatetime()
    groupString = f"{__normal}{__Fore.MAGENTA}{group}{__Style.RESET_ALL}"
    print(f"{__Fore.RED}{__Style.BRIGHT}CRITICAL{__Style.RESET_ALL}  {groupString} {__Style.RESET_ALL}{message}", end=end)

if __name__ == "__main__":
    logInfo("This is a test", "testGroup")