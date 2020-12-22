# Neo4j 基准测试指导文档

本文详细说明了如何在 `neo4j 3.5.19` 上完成图数据库基准测试项。

## 1. 安装Neo4j

详细安装文档参见[neo4j官方文档](https://neo4j.com/docs/operations-manual/3.5/docker/)，本节仅做**参考**

```shell
# 1.获取docker镜像
docker pull neo4j:3.5.19

# 2.创建3个目录，data 用来挂载数据库文件，import 用来存放导入的csv文件，plugins 用来存放neo4j算法jar包
cd $HOME/neo4j/
mkdir data
mkdir import
mkdir plugins

# 3.启动容器'$HOME/neo4j/*'分别对应上一步创建的三个目录
docker run -it -d --name neo4j --publish=7474:7474 --publish=7687:7687 -v $HOME/neo4j/import:/import -v $HOME/neo4j/data:/data -v $HOME/neo4j/plugins:/plugins neo4j:3.5.19
```

## 2. 导入数据

### 2.1 Graph500

1. 下载数据集

   ```
   wget https://www.galaxybase.com/public/download/graph500.zip
   ```

2. 解压数据集

   ```
   unzip graph500.zip
   ```

3. 移入指定目录

   ```
   # 移动数据源文件到挂载路径下的import目录下，例如启动Neo4j服务时设置了挂载路径 $HOME/neo4j/import，则需执行 mv graph500 $HOME/neo4j/import/
   
   mv graph500 $HOME/neo4j/import/
   ```

4. 进入docker容器

   ```bash
   docker exec -it neo4j bash
   ```

5. 执行导入脚本，等待导入完成

   导入``graph500-22``数据使用使用以下命令

   ```bash
   neo4j-admin import -database Graph500.db --id-type=ACTUAL -nodes:person=/import/graph500/vertex.csv -relationships:know=/import/graph500/edge.csv --delimiter='\t'
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

3. 移入指定目录

   ```
   # 移动数据源文件到挂载路径下的data目录下，例如启动Neo4j服务时设置了挂载路径 $HOME/neo4j/import，则需执行 mv twitter2010 $HOME/neo4j/import/
   
   mv twitter2010 $HOME/neo4j/import/
   ```

4. 进入docker容器

   ```bash
   docker exec -it neo4j bash
   ```

5. 执行导入脚本，等待导入完成

   导入``twitter2010``数据使用以下命令

   ```bash
neo4j-admin import --database Twitter-2010.db --id-type=ACTUAL -nodes:person=/import/twitter2010/v_header.csv,/import/twitter2010/vertex.csv -relationships:know=/import/twitter2010/e_header.csv,/import/twitter2010/edge.csv --delimiter='\t'
   ```

## 3. 修改配置文件

1. 进入docker容器

   ```bash
   docker exec -it neo4j bash
   ```

2. 打开配置文件

   ```bash
   vim conf/neo4j.conf
   ```

3. 需要修改的配置

   ```bash
   # 填写图名称（导入脚本中的参数`-database`指定了图名称
   dbms.active_database=Graph500.db
   # 添加了算法
   dbms.security.procedures.unrestricted=algo.*
   ```

4. 重启docker容器

   > tips：该docker版本导完数据之后，需要先给挂载的data目录一个读写权限，例如：``chmod 777 -R $HOME/neo4j/data``，否则会导致容器重启出错。

   ```bash
   # 退出docker容器
   exit
   # 重启neo4j
   docker restart neo4j
   ```

5. 登录Neo4j可视化界面

   浏览器内输入``服务器ip：7474``进入Neo4j可视化界面，初次登录默认用户名为``admin``，密码为``admin``，首次登录后会要求修改密码。

## 4. 运行测试脚本

### 4.1 打包测试项目

1. 为保证能够整体打包完成，切换到 `galaxybase-developer` 目录，运行以下语句，将 `lib` 下的 `galaxybase-bolt-driver-3.0.1.jar` 添加到本地仓库中

   ```shell
   mvn install:install-file -DgroupId=com.galaxybase.developer -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=lib\galaxybase-bolt-driver-3.0.1.jar
   ```
   
2. `cd`至项目根目录，运行 `mvn package`，获得 jar 包：`benchmark-neo4j-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

   ```shell
   mvn package
   ```


### 4.2 执行测试

#### 4.2.1 Graph500

1. 将测试jar包 `benchmark-neo4j-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移动到`Graph500`目录
2. 将 `application.yaml` 中的 ``IP`` 改为部署Neo4j数据库的服务器IP，将``username``和``password``分别修改为Neo4j登录用户名和密码
3. 使用 `java -jar benchmark-neo4j-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 4.2.2 Twitter-2010

1. 将测试jar包 `benchmark-neo4j-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移动到`Twitter-2010`目录
2. 将 `application.yaml` 中的 ``IP`` 改为部署Neo4j数据库的服务器IP，将``username``和``password``分别修改为Neo4j登录用户名和密码
3. 使用 `java -jar benchmark-neo4j-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见根项目下的README.md*