# Nebula 基准测试指导文档

本文详细说明了如何在 `Nebula 1.0.1` 上完成图数据库基准测试项。

## 1. 安装 Nebula

```shell
#详细安装步骤请参考：https://docs.nebula-graph.com.cn/manual-CN/3.build-develop-and-administration/2.install/1.install-with-rpm-deb/

#简略步骤如下
#1.下载安装包
# 本文为Ubuntu环境，采用 nebula-1.0.1.ubuntu1604.amd64.deb 包
下载地址：https://github.com/vesoft-inc/nebula/releases/tag/v1.0.1

# ubuntu 安装命令
sudo dpkg -i nebula-**.deb

# Centos 安装命令
sudo rpm -ivh nebula-**.deb

#5.启动 Nebula 服务
sudo /usr/local/nebula/scripts/nebula.service start all

#6.查看 Nebula 状态
sudo /usr/local/nebula/scripts/nebula.service status all
```



## 2. 导入数据

本次测试使用 Nebula 提供的 `Nebula Importer` 工具进行导入，使用docker安装 `Nebula Importer` 工具：

```shell
sudo docker pull vesoft/nebula-importer
```

`Nebula Importer` 使用文档：https://github.com/vesoft-inc/nebula-importer/blob/master/README_zh-CN.md



### 2.1 Graph500

 1. 进入 Nebula 命令行，创建 `Graph500` 图

    ```shell
    sudo /usr/local/nebula/bin/nebula -u root -p nebula
    
    # 进入命令行后输出如下命令创建图
    DROP SPACE Graph500;
    CREATE SPACE Graph500;
    USE Graph500;
    CREATE TAG ve(id int);
    CREATE EDGE ed();
    ```

 2. 下载数据集

    ```
    wget https://www.galaxybase.com/public/download/graph500.zip
    ```

 3. 解压数据集

    ```
    unzip graph500.zip
    ```

 4. 将测试数据集移动到本文提供的 `graph500.yaml` 文件所在的路径

    ```bash
    cd graph500
    mv vertex.csv 导入配置文件所在的目录
    mv edge.csv 导入配置文件所在的目录
    ```

 5. 数据集预处理，删除文件首行的 Head

    ```shell
    sed -i '1d' vertex.csv edge.csv
    ```
    
 6. 执行导入命令，等待导入完成

    ```bash
    sudo docker run --rm -ti -v 数据集所在路径:/graph500/  vesoft/nebula-importer --config /graph500/graph500.yaml
    ```

### 2.2 Twitter-2010

1. 进入 Nebula 命令行，创建 `Twitter-2010` 图

1. 下载数据集

   ```
   wget https://www.galaxybase.com/public/download/twitter2010.zip
   ```

2. 解压数据集

   ```
   unzip xf twitter2010.zip
   ```

3. 将测试数据集移动到本文提供的 `twitter-2010.yaml` 文件所在的路径

   ```shell
   cd twitter2010
   mv vertex.csv 导入配置文件所在的目录
   mv edge.csv 导入配置文件所在的目录
   ```

4. 执行导入脚本，等待导入完成

   ```bash
   sudo docker run --rm -ti -v 数据集所在路径:/twitter2010/  vesoft/nebula-importer --config /twitter2010/twitter-2010.yaml
   ```

*Nebula 的数据导入流程可参考详细文档：https://github.com/vesoft-inc/nebula-importer/blob/master/README_zh-CN.md*



## 3.运行测试脚本

### 3.1 打包测试项目

1. 切换到`galaxybase-developer/lib`目录，运行以下语句，将 `lib` 下的 `galaxybase-bolt-driver-3.0.1.jar` 添加到本地仓库中

   ```shell
   mvn install:install-file -DgroupId=com.galaxybase.developer -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=lib\galaxybase-bolt-driver-3.0.1.jar
   ```

2. `cd`到项目根目录运行 `mvn package`，获得 jar 包：`benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

   ```shell
   mvn package
   ```

### 3.2 执行测试

#### 3.2.1 Graph500

1. 将测试jar包 `benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Graph500` 目录；
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP；
3. 使用 `java -jar benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 3.2.2 Twitter-2010

1. 将测试jar包 `benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 移到 `Twitter-2010` 目录；
2. 将 `application.yaml` 中的 IP 改为连接的图服务的IP；
3. 使用 `java -jar benchmark-nebula-1.0.0-SNAPSHOT-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。



*application.yaml文件是本项目的配置文件，可以指定图名称、样本集、测试项以及测试次数等，其配置文档详见README.md*

