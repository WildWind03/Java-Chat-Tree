#!/bin/bash

startRoot() {
		cd ./root
		java -jar chattree-0.1.jar First 50 12000
}

gradle jar
cd ./build/libs
cp ./chattree-0.1.jar ./root
cp ./chattree-0.1.jar ./child
rm ./root/client_log.out
rm ./child/client_log.out

startRoot &

cd ./child
java -jar chattree-0.1.jar Second 50 12001 localhost 12000