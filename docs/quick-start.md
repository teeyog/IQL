# Quickstart

### 1. 拉取代码
```
git clone https://github.com/teeyog/IQL.git
```
### 2. 修改配置文件，文件中已列出必填项
```
修改web模块配置文件 iql-web\src\main\resources\application.properties

修改engine模块配置文件 iql-engine\src\main\resources\iql.properties
```
### 3. 导入web模块所需元数据表：[iql.sql](https://github.com/teeyog/IQL/blob/master/docs/file/iql.sql)

### 4. 编译打包
```
cd IQL/
mvn clean package -Dmaven.test.skip
cd iql-web\target # 获取iql-web.jar
cd iql-engine\target # 获取iql-engine.jar
```

### 5. 运行
```
web 模块启动示例
java -jar iql-web.jar

web地址 localhost:88888
user:admin password:123456

engine 模块启动示例
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

> 注意：engine 启动命令中的 fairscheduler.xml文件在这里：[fairscheduler.xml](https://github.com/teeyog/IQL/blob/master/docs/file/fairscheduler.xml)
