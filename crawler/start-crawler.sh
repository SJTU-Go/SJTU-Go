killall python3
nohup python3 -u /home/apiuser/crawler/crawler_script_hello.py > crawler_hello.log 2>&1 &
nohup python3 -u /home/apiuser/crawler/crawler_script_e100.py > crawler_e100.log 2>&1 &
nohup python3 -u /home/apiuser/crawler/crawler_script_cluster.py > crawler_hello.log 2>&1 &
nohup python3 -u /home/apiuser/crawler/crawler_script_jindouyun.py > crawler_jdy.log 2>&1 &