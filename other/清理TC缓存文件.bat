@echo off
rem ��    ;�����TC�����ļ�
rem ��    �ߣ�kai

rem ��ȡTC����Ŀ¼
set tc_path=%FMS_HOME%
set tc_path=%tc_path:fcc=%
for /f "tokens=*" %%i in ("%tc_path%") do cd %tc_path%

rem if exist "%tc_path%\fsc\fscadmin.bat" goto :Server_clean

taskkill /f /im javaw.exe
taskkill /f /im Teamcenter.exe
tasklist|findstr "Teamcenter.exe"
if %errorlevel%==0 (goto next)

taskkill /f /im java.exe
taskkill /f /im java.exe
taskkill /f /im java.exe
taskkill /f /im javaw.exe
taskkill /f /im Teamcenter.exe

  
echo *****************************************
echo �������TeamCenter������ʱ�ļ������Ե�......
echo *****************************************          
del /f /s /q "%userprofile%\FCCCache\*.*" 
rd /s /q "%userprofile%\FCCCache\"   
del /f /s /q "%userprofile%\Oracle\*.*" 
rd /s /q "%userprofile%\Oracle\" 
del /f /s /q "%userprofile%\Siemens\*.*" 
rd /s /q "%userprofile%\Siemens\" 
del /f /s /q "%userprofile%\Teamcenter\*.*" 
rd /s /q "%userprofile%\Teamcenter\"
echo *****************************************
echo ���TeamCenter������ʱ�ļ���ɣ�  
echo *****************************************

echo *****************************************
echo �������temp�����Ե�......
echo *****************************************          
del /f /s /q %tmp%\*.*
echo *****************************************
echo ���temp��ɣ�  
echo *****************************************

rem echo �������TeamCenter 2-Tier�ֿͻ�����ʱ�ļ������Ե�......
rem set tc_path=%FMS_HOME%
rem set tc_path=%tc_path:fcc=%
rem for /f "tokens=*" %%i in ("%tc_path%") do cd %tc_path%
rem del /f /s /q "%tc_path%\portal\temp\*.*"
rem rd /s /q "%tc_path%\portal\temp\"
rem md  "%tc_path%\portal\temp\" 
rem echo ���TeamCenter 2-Tier�ֿͻ�����ʱ�ļ���ɣ�

echo *****************************************
echo ���TeamCenter 2-Tier�ֿͻ�����ʱ�ļ���ɣ�
echo *****************************************
  
echo. & pause 
exit

:next
msg /W /TIME:300 %username% TeamCenter�����������У���رպ���������棡
exit


:Server_clean
msg /W /TIME:300 %username% ��������������ʹ�ô˽ű������ֹ�������棡
exit