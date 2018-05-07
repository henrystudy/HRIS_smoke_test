echo "Run test from testng.xml..."
cd D:\eclipse_workspace\ad smoke test
java -cp "./bin;./libs/*;C:/Users/zhenhaiw/.p2/pool/plugins/*" org.testng.TestNG ./testng.xml
pause