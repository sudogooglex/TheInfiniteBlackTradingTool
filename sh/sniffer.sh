sleepTime=0;
finalSleep=0;

echo "* Recovering Last Data in $sleepTime s ...\n"
sleep $sleepTime;
curl 'http://www.theinfiniteblack.com/community/tib/leaderboards/AH.aspx?playername=&server=3' -H 'Host: www.theinfiniteblack.com' -H 'User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:38.0) Gecko/20100101 Firefox/38.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' -H 'Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3' --compressed -H 'DNT: 1' -H 'Referer: http://www.theinfiniteblack.com/community/tib/leaderboards/AH.aspx?playername=&server=3' -H 'Connection: keep-alive' --data '__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUJNzg1NDQ5Mzc5D2QWAgIDD2QWAgIND2QWBGYPZBYEAgMPFCsAAmRkZAIFDw9kPCsABgEAEBYBZhYBFgIeDlBhcmFtZXRlclZhbHVlBQEzFgECBGQCAQ9kFgoCAQ88KwAKAQAPFgIeAlNEFgEGAEgZPhuI0ohkZAIDDxBkZBYBZmQCBQ8QZGQWAWZkAgcPFCsAAmRkZAIJDw9kPCsABgEAEBYDZgIBAgIWAxYCHwAFATMWAh8ABgBIGT4biNKIFgIfAGUWAwIEZmZkGAIFCUxpc3RWaWV3MQ9nZAUJTGlzdFZpZXcyD2dk8itmnfbrTVV6ICpyijQvOl5OFaTnc3npRBzFR1RoTEA%3D&__VIEWSTATEGENERATOR=776D544F&__EVENTVALIDATION=%2FwEdAANAFiSjldBnqG%2FkFPSYL6Oyzfg78Z8BXhXifTCAVkevdz9YyyR4qjX0bKJ2X6bXV1Mf881tbuY%2BSHVahftvQ6O3P0cT1D9FoG%2F6%2F61f7kIj0w%3D%3D&radion-normal=3&Button2=See+History+Archive'>outSniffer.html
echo "\n\n* Done ! Sleeping $finalSleep s ...\n"
sleep $finalSleep;

