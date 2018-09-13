pid=`lsof -i :9000 | grep LISTEN | awk '{print $2}'`
if [ "$pid" ];then
echo $pid
kill -9 $pid
fi
echo 'Now, the program was shutdown!'