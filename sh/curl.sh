sleepTime=0;
finalSleep=0;

echo "* Recovering Last Data in $sleepTime s ...\n"
sleep $sleepTime;
curl 'http://www.theinfiniteblack.com/community/tib/leaderboards/AH.aspx?playername=&server=3' -H 'Host: www.theinfiniteblack.com' -H 'User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:38.0) Gecko/20100101 Firefox/38.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' -H 'Accept-Language: en-US,en;q=0.5' --compressed -H 'DNT: 1' -H 'Referer: http://www.theinfiniteblack.com/community/tib/leaderboards/AH.aspx?playername=&server=3' -H 'Cookie: __utma=63357290.2070854145.1435608360.1435624960.1435690115.5; __utmz=63357290.1435608360.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmb=63357290.83.10.1435690115; __utmc=63357290; ASP.NET_SessionId=zpnbane3g2lplk2vkel0g4db; __utmt=1' -H 'Connection: keep-alive' --data '__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUJNzg1NDQ5Mzc5D2QWAgIDD2QWAgIND2QWBGYPZBYEAgMPFCsAAmRkZAIFDw9kPCsABgEAEBYBZhYBFgIeDlBhcmFtZXRlclZhbHVlBQEzFgECBGQCAQ9kFgoCAQ88KwAKAQAPFgIeAlNEFgEGAIhhwAiB0ohkZAIDDxBkZBYBZmQCBQ8QZGQWAWZkAgcPFCsAAmRkZAIJDw9kPCsABgEAEBYDZgIBAgIWAxYCHwAFATMWAh8ABgCIYcAIgdKIFgIfAGUWAwIEZmZkGAIFCUxpc3RWaWV3MQ9nZAUJTGlzdFZpZXcyD2dk7w2u1mYbnrcSC%2BpsRL5BvKfFV31XzDGsBv0mqld4Tt8%3D&__VIEWSTATEGENERATOR=776D544F&__EVENTVALIDATION=%2FwEdAAOA36fWLCpk1npjLssp0z3Izfg78Z8BXhXifTCAVkevdz9YyyR4qjX0bKJ2X6bXV1Ps8K2TxH%2Bh3Z4XV1iJd6nfNSNYlQVUSTmq8lublncrPA%3D%3D&radion-normal=3&Button1=View+Current+Sales'>out.html
echo "\n\n* Done ! Sleeping $finalSleep s ...\n"
sleep $finalSleep;
#>out.html

