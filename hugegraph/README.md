# HugeGraph 基准测试指导文档

本文详细说明了如何在 `HugeGraph 0.10.4` 上完成图数据库基准测试项。

## 1. 安装 HugeGraph 

```shell
#详细安装步骤请参考：https://hugegraph.github.io/hugegraph-doc/quickstart/hugegraph-server.html

#简略步骤如下
#1.下载安装包
下载地址：https://hugegraph.github.io/hugegraph-doc/download.html

#2.解压安装包
tar -zxf hugegraph-0.10.4.tar.gz

#3.进入目录
cd hugegraph-0.10.4/conf

#4.测试时超时时间最大为1小时，修改 HugeGraph 默认超时时间为1小时以上
#4.1 修改 gremlin-server.yaml 中 scriptEvaluationTimeout 配置项
scriptEvaluationTimeout: 3600000
#4.2 rest-server.properties 添加如下配置项
gremlinserver.timeout=3600
restserver.request_timeout=-1
```



## 2. 导入数据

导入基于 `HugeGraph-Loader` 工具，本文采用 `0.10.0` 版本

```shell
# HugeGraph-Loader 下载地址：https://hugegraph.github.io/hugegraph-doc/download.html

# 解压安装包
tar -zxf hugegraph-loader-0.10.0.tar.gz
```



### 2.1 Graph500

 1. 下载数据集

    ```
    wget https://www.galaxybase.com/public/download/graph500.zip
    ```

 2. 解压数据集

    ```
    unzip graph500.zip
    ```

 4. 将数据集移动到本文提供的两个导入配置 `struct.json` 和 `schema.groovy` 文件所在的目录

    ```shell
    cd graph500
    mv vertex.csv 导入配置文件所在的目录
    mv edge.csv 导入配置文件所在的目录
    ```
    
 4. 数据集预处理，进行格式转换

    ```bash
    sed -i '1d;s/\t/,/g' vertex.csv edge.csv
    ```

5. 执行导入命令，等待导入完成

   ```shell
   $HUGEGRAPH-LOADER/bin/hugegraph-loader -g hugegraph -f struct.json -s schema.groovy
   ```

   

### 2.2 Twitter-2010

1. 下载数据集

   ```
   wget https://www.galaxybase.com/public/download/twitter2010.zip
   ```

2. 解压数据集

   ```
   unzip twitter2010.zip
   ```

3. 将数据集移动到本文提供的两个导入配置 `struct.json` 和 `schema.groovy` 文件所在的目录

   ```
   cd twitter2010
   mv vertex.csv 导入配置文件所在的目录
   mv edge.csv 导入配置文件所在的目录
   ```

4. 数据集预处理，进行格式转换

   ```bash
   sed -i 's/\t/,/g' vertex.csv edge.csv
   ```

5. 执行导入命令，等待导入完成

   ```shell
   $HUGEGRAPH-LOADER/bin/hugegraph-loader -g hugegraph -f struct.json -s schema.groovy
   ```

   

*HugeGraph 的数据导入流程可参考详细文档：https://hugegraph.github.io/hugegraph-doc/quickstart/hugegraph-loader.html*



## 3.运行测试脚本

### 3.1 打包测试项目

1. 为保证能够整体打包完成，切换到 `galaxybase-developer` 目录，运行以下语句，将 `lib` 下的 `galaxybase-bolt-driver-3.0.1.jar` 添加到本地仓库中

   ```shell
   mvn install:install-file -DgroupId=com.galaxybase.developer -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=lib\galaxybase-bolt-driver-3.0.1.jar
   ```

2. `cd`到项目根目录运行 `mvn package`，获得 jar 包：`benchmark-hugegraph-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

   ```shell
   mvn package
   ```


### 3.2 执行测试

#### 3.2.1 Graph500

1. 将测试jar包 `benchmark-hugegraph-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Graph500` 目录；
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP；
3. 使用 `java -jar benchmark-hugegraph-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 3.2.2 Twitter-2010

1. 将测试jar包 `benchmark-hugegraph-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Twitter-2010` 目录；
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP；
3. 使用 `java -jar benchmark-hugegraph-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。



*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见README.md*

