pid=`lsof -i :9000 | grep LISTEN | awk '{print $2}'`
if [ "$pid" ];then
echo $pid
kill -9 $pid
rm -rf ocr-log.log
nohup java -jar ocr/target/ocr-1.0-SNAPSHOT.jar >ocr-log.log 2>&1 &
else
nohup java -jar ocr/target/ocr-1.0-SNAPSHOT.jar >ocr-log.log 2>&1 &
fi
echo 'Now, the program is running!'
