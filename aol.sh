#!/bin/bash
set -e

usage() {
 cat <<-USAGE
Usage:
    -i install aol cli to device
    -s device serial
    -o option of aol, one of [class, instance]
    -c class name
    -h help
USAGE
}

aol_apk=
device=
option=
class=

while getopts i:s:o:c:h arg; do
  case $arg in
    i) aol_apk=$OPTARG ;;
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
if [[ $abi == x86* ]]; then
  abi="x86"
fi

target_dir="/data/local/tmp"
so="libaol-art.so"
target_so="$target_dir/$so"
target_aol="$target_dir/aol"

install() {
  temp="cli-temp"
  unzip -q $1 -d $temp
  echo "$temp/lib/$abi/$so"
  $cmd push $temp/lib/$abi/$so $target_so
  $cmd push $temp/classes.dex $target_aol
  rm -rf $temp
}

dump() {
  $cmd shell app_process -Djava.class.path=$target_aol -Djava.library.path=$target_dir / com.chaos.aol.cli.Main $1 $2
}

if [ -n "$aol_apk" ]; then
    install $aol_apk
fi

if [ -z "$option" ]; then
  if [ -z "$aol_apk" ]; then
    exit 1
  else
    exit 0
  fi
else
  dump $option $class
fi