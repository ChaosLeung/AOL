#!/bin/bash
set -e

usage() {
 cat <<-USAGE
Usage:
    -i install aol to device
    -s device serial
    -o option of aol, one of [class, instance]
    -c class name
    -h help
USAGE
}

run_install=0
device=
option=
class=

while getopts is:o:c:h arg
do
  case $arg in
    i) run_install=1 ;;
    s) device=$OPTARG ;;
    o) option=$OPTARG ;;
    c) class=$OPTARG ;;
    h) usage; exit 0 ;;
    *) echo "Unknown argument"; usage; exit 1;
  esac
done

dir=$(dirname "$0")
cmd=

if [ -z "$device" ]; then
  cmd="adb "
else
  cmd="adb -s $device "
fi

abi=$($cmd shell getprop ro.product.cpu.abi)
if [[ $abi = x86* ]]; then
  abi="x86"
fi

target_dir="/data/local/tmp"
so="libaol-art.so"
target_so="$target_dir/$so"
target_aol="$target_dir/aol"

install_aol() {
  bash $dir/gradlew -q :cli:assembleRelease
  temp="cli-temp"
  unzip -q $dir/cli/build/outputs/apk/release/cli-release-unsigned.apk -d $temp
  echo "$temp/lib/$abi/$so"
  $cmd push $temp/lib/$abi/$so $target_so
  $cmd push $temp/classes.dex $target_aol
  rm -rf $temp
}

dump() {
  $cmd shell app_process -Djava.class.path=$target_aol -Djava.library.path=$target_dir / com.chaos.aol.cli.Main $1 $2
}

if [ $run_install -eq 1 ]; then
    install_aol
fi

if [ -z "$option" ]; then
  exit 1
else
  dump $option $class
fi