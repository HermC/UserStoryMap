#!/usr/bin/env bash
# !/bin/sh
#
# 该脚本为Linux下启动java程序的通用脚本，即可以作为开机自启动service脚本被调用，
# 也可以作为启动java程序的独立脚本来使用。
#
#
# 警告！！！该脚本stop部分使用系统kill命令来强制终止指定的java程序进程。
# 在杀死进程前，未做任何条件检查。在某些情况下，如程序正在进行文件或数据库写操作，
# 可能会造成数据丢失或数据不完整，如果必须要考虑这类情况，则需要改写此脚本，
# 增加在执行kill命令前的一系列检查。
#
#
#########################################################
# 环境变量及程序执行参数
# 需要根据实际环境以及java程序的名称来修改这些参数
#########################################################
# JDK所在路径
export LANG=en_US.UTF-8
JAVA_HOME=$JAVA_HOME
# 执行程序启动所使用的系统用户，考虑到安全，不推荐使用root账号
RUNNING_USER=root

# java程序所在的目录(classes的上一级目录)
# APP_HOME=/home/ubuntu/

# 需要启动的java主程序(main方法类)
APP_MAINCLASS=edu.nju.usm.UsmApplication
APP_MAINJAR=usm-0.0.1-SNAPSHOT.jar
# APP_LIB=lib
# APP_CONFIG=confg

# 拼凑完整的classpath参数，包括指定目录下所有的jar
# CLASSPATH=$APP_HOME/classes
# for i in "$APP_HOME"/lib/*.jar; do
#   CLASSPATH="$CLASSPATH":"$i"
# done

# CLASSPATH=$CLASSPATH:$APP_HOME/config

# java虚拟机启动参数
# JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"
JAVA_OPTS="-Xms128M -Xmx256M -XX:PermSize=64M -XX:MaxPermSize=128M"

#########################################################
# (函数)判断程序是否已经启动
#
# 说明:
# 使用JDK自带的JPS命令以及grep命令组合，准确查找pid
#########################################################
psid=0

checkpid() {
    path=`pwd`
    javaps=`jps -l -m | grep $APP_MAINJAR`
    if [ -n "$javaps" ]; then
        echo $javaps
        psid=`echo $javaps | awk '{print $1}'`
    else
        psid=0
    fi

    if [ $psid = "pwd" ]; then
        psid=0
    fi
}

#########################################################
# (函数)启动程序
#
# 说明:
# 1. 首先调用checkpid函数，刷新$psid全局变量
# 2. 如果程序已经启动($psid不等于0)，则提示程序已经启动
# 3. 如果程序没有被启动，则执行启动命令行
# 4. 启动执行命令行后，再次调用checkpid函数
# 5. 如果步骤4的结果能够确认程序的pid，则打印[OK]，否则打印[Failed]
# 注意: echo -n 表示打印字符后，不换行
# 注意: "nohup 某命令 > /dev/null 2>&1 &的用法"
#########################################################
start() {
    checkpid

    if [ $psid -ne 0 ]; then
        echo "======================================================================================"
        echo "[WARNING] $APP_MAINJAR already started! (pid=$psid)"
        echo "======================================================================================"
    else
        echo -n "Starting $APP_MAINJAR ..."
        JAVA_CMD="nohup java $JAVA_OPTS -jar $APP_MAINJAR > /dev/null 2>&1 &"
        # JAVA_CMD=" $JAVA_HOME/bin/java $JAVA_OPTS -cp .:$APP_LIB/*:$APP_MAINJAR $APP_MAINCLASS `pwd` > /dev/null 2>&1 &"
        echo $JAVA_CMD
        nohup java $JAVA_OPTS -jar $APP_MAINJAR > /dev/null 2>&1 &
        # $JAVA_HOME/bin/java $JAVA_OPTS -cp .:$APP_LIB/*:$APP_MAINJAR $APP_MAINCLASS `pwd` > /dev/null 2>&1 &
        # $JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_MAINJAR
        # $JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_MAINJAR 2>&1 &
        # su - $RUNNING_USER -c "$JAVA_CMD"
        checkpid
        if [ $psid -ne 0 ];then
            echo "(pid=$psid) [OK]"
        else
            echo "[Failed]"
        fi
    fi
}

#########################################################
# (函数)停止程序
#
# 说明:
# 1. 首先调用checkpid函数，刷新$psid全局变量
# 2. 如果程序已经启动($psid不等于0)，则开始执行停止，否则，提示程序未执行
# 3. 使用kill -9 pid命令进行强制杀死进程
# 4. 执行kill命令行紧接其后，马上查看上一句命令的返回值: $?
# 5. 如果步骤4的结果$?等于0，则打印[OK]，否则打印[Failed]
# 6. 为了防止java程序被启动多次，这里增加反复检查进程，反复杀死进程的处理(递归调用stop)
# 注意: echo -n 表示打印字符后，不换行
# 注意: 在shell编程中，"$?"表示上一句命令或者一个函数的返回值
#########################################################
stop() {
    checkpid

    if [ $psid -ne 0 ]; then
        echo -n "Stopping $APP_MAINJAR ...(pid=$psid) "
        # su - $RUNNING_USER -c "kill -9 $psid"
        kill -9 $psid
        if [ $? -eq 0 ]; then
            echo "[OK]"
        else
            echo "[Failed]"
        fi

        checkpid
        if [ $psid -ne 0 ]; then
            stop
        fi
    else
        echo "======================================================================================"
        echo "[WARNING] $APP_MAINJAR is not running"
        echo "======================================================================================"
    fi
}

#########################################################
# (函数)安全停止程序
#
# 说明:
# 1. 首先调用checkpid函数，刷新$psid全局变量
# 2. 如果程序已经启动($psid不等于0)，则开始执行停止，否则，提示程序未执行
# 3. 使用kill -15 pid命令进行强制杀死进程
# 4. 执行kill命令行紧接其后，马上查看上一句命令的返回值: $?
# 5. 如果步骤4的结果$?等于0，则打印[OK]，否则打印[Failed]
# 6. 为了防止java程序被启动多次，这里增加反复检查进程，反复杀死进程的处理(递归调用stop)
# 注意: echo -n 表示打印字符后，不换行
# 注意: 在shell编程中，"$?"表示上一句命令或者一个函数的返回值
# 注意: 此方法可能无法完全停止程序
#########################################################
safestop() {
    echo "[WARNING] This operation may be ignored by the process. If happened, use 'stop' operation instead."

    checkpid

    if [ $psid -ne 0 ]; then
        echo -n "Stopping $APP_MAINJAR ...(pid=$psid) "
        # su - $RUNNING_USER -c "kill -15 $psid"
        kill -15 $psid
        if [ $? -eq 0 ]; then
            echo "[OK]"
        else
            echo "[Failed]"
        fi

        checkpid
        if [ $psid -ne 0 ]; then
            safestop
        fi
    else
        echo "======================================================================================"
        echo "[WARNING] $APP_MAINJAR is not running"
        echo "======================================================================================"
    fi
}

#########################################################
# (函数)检查程序运行状态
#
# 说明:
# 1. 首先调用checkpid函数，刷新$psid全局变量
# 2. 如果程序已经启动($psid不等于0)，则提示正在运行并表示出pid
# 3. 否则，提示程序未运行
#########################################################
status() {
    checkpid

    if [ $psid -ne 0 ]; then
        echo "[INFO] $APP_MAINJAR is running! (pid=$psid)"
    else
        echo "[INFO] $APP_MAINJAR is not running"
    fi
}

#########################################################
# (函数)打印系统环境参数
#########################################################
info() {
    echo "******************************************************************"
    echo "System Information: "
    echo "******************************************************************"
    echo `head -n 1 /etc/issue`
    echo `uname -a`
    echo
    echo "JAVA_HOME=$JAVA_HOME"
    echo
    echo "APP_MAINJAR=$APP_MAINJAR"
    # echo "APP_LIB=$APP_LIB"
    # echo "APP_CONFIG=$APP_CONFIG"
    echo "APP_MAINCLASS=$APP_MAINCLASS"
    echo "******************************************************************"
}

#########################################################
# 读取脚本的第一个参数($1)，进行判断
# 参数取值范围: (start|stop|restart|status|info)
# 如果参数不在指定范围之内，则打印帮助信息
#########################################################
case "$1" in
    'start')
        start
        ;;
    'stop')
        stop
        ;;
    'safestop')
        safestop
        ;;
    'restart')
        stop
        start
        ;;
    'status')
        status
        ;;
    'info')
        info
        ;;
    *)
        echo "[INFO] Usage: $0 {start|stop|safestop|restart|status|info}"
        exit 1
        ;;
esac
exit 0