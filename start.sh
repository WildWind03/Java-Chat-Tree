#!/bin/bash

startRoot() {
		cd ./first
		java -jar chattree-0.1.jar First 50 12000
}

startSecond() {
    cd ./second
    java -jar chattree-0.1.jar Second 50 12001 localhost 12000

}

startThird() {
    cd ./third
    java -jar chattree-0.1.jar Third 50 12002 localhost 12001

}

killSecond() {
    sleep 2
    kill -9 $(lsof -ti:12001)

}

gradle jar
cd ./build/libs
cp ./chattree-0.1.jar ./first
cp ./chattree-0.1.jar ./second
cp ./chattree-0.1.jar ./third
rm ./first/client_log.out
rm ./second/client_log.out
rm ./third/client_log.out

startRoot &
startSecond &
startThird &

killSecond &

sleep 4

kill -9 $(lsof -ti:12000)

kill -9 $(lsof -ti:12002)