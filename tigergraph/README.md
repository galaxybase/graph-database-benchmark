# TigerGraph 基准测试指导文档

本文详细说明了如何在 `tiger 2.5.3` 上完成图数据库基准测试项。

## 1. 安装Tiger

安装详情查阅[tiger官方文档](https://docs.tigergraph.com.cn/admin/admin-guide/installation-and-configuration/installation-guide)

1. 下载安装包

   ~~~shell
   wget https://dl.tigergraph.com/developer-edition/tigergraph-2.5.3-developer.tar.gz
   ~~~

2. 解压安装包，解压完成之后会生成一个 `tigergraph-2.5.3-developer` 目录，进入该目录

   ~~~shell
   tar -zxvf tigergraph-2.5.3-developer.tar.gz
   cd tigergraph-2.5.3-developer
   ~~~

3. 执行安装脚本安装tiger，安装过程中会询问用户是否需要修改配置，直接回车使用默认配置安装

   ~~~shell
   sudo ./install.sh -s
   ~~~

4. 切换到 `tigergraph` 用户修改请求响应超时时间

   ~~~shell
   # 切换到 tigergraph 用户
   su tigergraph
   # 将超时全部修改为 3600(1小时)
   gadmin --configure timeout
   # 设置配置生效
   gadmin config-apply
   # 重启tiger
   gadmin restart
   ~~~

## 2. 导入数据

### 2.1 Graph500

1. 下载数据集

   ```shell
   wget https://www.galaxybase.com/public/download/graph500.zip
   ```

2. 解压数据集

   ```shell
   unzip graph500.zip
   ```

3. 移入指定目录

   ```shell
   # 将数据移动到指定目录
   mv graph500 /home/tigergraph/data/
   ```
   
4. 将 `Graph500/gsql` 目录下的 `graph500.gsql` 文件上传到服务器中的 `/home/tigergraph/data/graph500/` 目录下

5. 切换到 `tigergraph` 用户，运行 `gsql文件` 导入数据和安装查询

   ~~~shell
   gsql graph500.gsql
   ~~~

### 2.2 Twitter-2010

1. 下载数据集

   ```
   wget https://www.galaxybase.com/public/download/twitter2010.zip
   ```

2. 解压数据集

   ```
   unzip twitter2010.zip
   ```

3. 移入指定目录

   ```shell
   # 将数据移动到指定目录
   mv twitter2010 /home/tigergraph/data/
   ```
   
4. 将 `Twitter-2010/gsql` 目录下的 twitter2010.gsql` 文件上传到服务器中的 `/home/tigergraph/data/twitter2010/` 目录下

5. 运行 `gsql文件` 导入数据和安装查询

   ~~~shell
   gsql twitter2010.gsql
   ~~~

## 3. 运行测试脚本

### 3.1 打包测试项目

1. 为保证能够整体打包完成，切换到 `galaxybase-developer` 目录，运行以下语句，将 `lib` 下的 `galaxybase-bolt-driver-3.0.1.jar` 添加到本地仓库中

   ~~~shell
   mvn install:install-file -DgroupId=com.galaxybase.developer -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=lib\galaxybase-bolt-driver-3.0.1.jar
   ~~~
   
2. `cd`至项目根目录，运行 `mvn package`，获得 jar 包：`benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

   ```shell
   mvn package
   ```


### 3.2 执行测试

#### 3.2.1 Graph500

1. 将 `tiger` 模块下的测试jar包 `benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移动到`Graph500`目录

   ~~~shell
   mv benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar Graph500/
   ~~~

2. 将 `application.yaml` 中的 IP 改为部署Tiger数据库的服务器IP

3. 使用 `java -jar benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成4个日志文件，为本次测试的结果。

#### 3.2.2 Twitter-2010

1. 将测试jar包 `benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移动到`Twitter-2010`目录

   ~~~shell
   mv benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar Twitter-2010/
   ~~~

   

2. 将 `application.yaml` 中的 IP 改为部署Tiger数据库的服务器IP

3. 使用 `java -jar benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成4个日志文件，为本次测试的结果。



*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见根项目下的README.md*