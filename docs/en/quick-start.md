# Quickstart

### 1. Pull code
```
git clone https://github.com/teeyog/IQL.git
```
### 2. Modify the configuration file, the required fields are listed in the file
```
modify the web module configuration file iql-web\src\main\resources\application.properties

modify the engine module configuration file iql-engine\src\main\resources\iql.properties
```
### 3. Import metadata table required for web module：[iql.sql](https://github.com/teeyog/IQL/blob/master/docs/file/iql.sql)

### 4. Compile and package
```
cd IQL/
mvn clean package -Dmaven.test.skip
cd iql-web\target # 获取iql-web.jar
cd iql-engine\target # 获取iql-engine.jar
```

### 5. Run
```
web module startup example: 
java -jar iql-web.jar

web address localhost:8888
user:admin password: 123456

engine module startup example:
sudo -u hdfs spark2-submit \
	--name IQL \
	--master yarn \
	--deploy-mode client \
	--conf spark.executor.extraJavaOptions="-Xms1024m  -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m" \
	--files /home/runtime_file/fairscheduler.xml \
	--num-executors 1 \
	--driver-memory 3G \ 
	--executor-memory 1g \   
	--class iql.engine.main.IqlMain \
	/home/run/jobJar/iql-engine.jar
```

> Note：the fairscheduler.xml file in the engine startup command is here：[fairscheduler.xml](https://github.com/teeyog/IQL/blob/master/docs/file/fairscheduler.xml)
