@echo off
rem 用    途：清除TC缓存文件
rem 作    者：kai

rem 读取TC运行目录
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
echo 正在清除TeamCenter程序临时文件，请稍等......
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
echo 清除TeamCenter程序临时文件完成！  
echo *****************************************

echo *****************************************
echo 正在清除temp，请稍等......
echo *****************************************          
del /f /s /q %tmp%\*.*
echo *****************************************
echo 清除temp完成！  
echo *****************************************

rem echo 正在清除TeamCenter 2-Tier胖客户端临时文件，请稍等......
rem set tc_path=%FMS_HOME%
rem set tc_path=%tc_path:fcc=%
rem for /f "tokens=*" %%i in ("%tc_path%") do cd %tc_path%
rem del /f /s /q "%tc_path%\portal\temp\*.*"
rem rd /s /q "%tc_path%\portal\temp\"
rem md  "%tc_path%\portal\temp\" 
rem echo 清除TeamCenter 2-Tier胖客户端临时文件完成！

echo *****************************************
echo 清除TeamCenter 2-Tier胖客户端临时文件完成！
echo *****************************************
  
echo. & pause 
exit

:next
msg /W /TIME:300 %username% TeamCenter程序正在运行，请关闭后再清除缓存！
exit


:Server_clean
msg /W /TIME:300 %username% 服务器环境不可使用此脚本，请手工清除缓存！
exit