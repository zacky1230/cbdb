#!/usr/bin/expect
set timeout 10
set src_file [lindex $argv 0]
set dest_file [lindex $argv 1]
spawn scp $src_file cbdb@192.168.20.192:$dest_file
 expect {
 "(yes/no)?"
  {
    send "yes\n"
    expect "*assword:" { send "ST11AlPfZmRv\n"}
  }
 "*assword:"
  {
    send "ST11AlPfZmRv\n"
  }
}
expect "100%"
expect eof