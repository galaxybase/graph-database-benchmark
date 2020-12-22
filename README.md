## Galaxybase开发者版基准测试

### 摘要

​		本次基准测试在单节点环境下进行，采用 Graph500 和 Twitter-2010 公开数据集，研究和分析对比了 Galaxybase 开发者版和其他图数据库系统的数据加载能力和查询性能。基准测试包括以下内容：

**数据加载测试**

1. 加载耗时
2. 加载后数据占用的磁盘容量

**数据查询测试**

1. K-hop neighbor 响应时间
2. Shortest Path 响应时间
3. PageRank 响应时间
4. Weakly Connected Components 响应时间
5. Label Propagation Algorithm 响应时间
6. Degree Centrality 响应时间



### 一、测试环境

本节描述了测试的图数据库系统以及所使用的硬件平台和数据集。

#### 1.1 图数据库系统

- Galaxybase Developer 3.0.2
- TigerGraph Developer 2.5.3
- Neo4j Community 3.5.19
- HugeGraph 0.10.4
- Nebula 1.0.1

#### 1.2 测试环境

##### 1.2.1 硬件平台

​		本次测试在单节点环境下，所有图数据库系统都采用相同配置的服务器进行测试，详细配置信息如下：

|  &nbsp;  |                  服务器配置                  |
| :------: | :------------------------------------------: |
| CPU参数  | 12  Intel(R) Core(TM) i7-7800X CPU @ 3.50GHz |
|   内存   |                  128G DDR4                   |
| 网络带宽 |                     千兆                     |
|   硬盘   |                5.5T 机械硬盘                 |

##### 1.2.2 软件环境

操作系统：Ubuntu 16.04.1 LTS（kernel版本：4.15.0-72-generic）

Java版本：Java SE 8 Update 201（build 1.8.0_201-b09）

Docker版本：Docker version 17.06.2

#### 1.3 数据集

测试采用两个公开数据集，详细信息如下：

|                           名称                            |   点数量   |    边数量     | 原始数据大小 |
| :-------------------------------------------------------: | :--------: | :-----------: | :----------: |
|              [Graph500](http://graph500.org)              | 2,396,019  |  67,108,864   |    1008MB    |
| [Twitter-2010](http://an.kaist.ac.kr/traces/WWW2010.html) | 41,652,230 | 1,468,365,182 |    24.6GB    |



### 二、数据加载测试

数据加载测试检查了以下两个方面：

1. 加载耗时
2. 加载后数据占用的磁盘容量

#### 2.1 加载方法

​	对于每个图数据库，我们选择了最有利的批量加载数据的方法：

| 图数据库名称 |            加载时使用的API或方法            |
| :----------: | :-----------------------------------------: |
|  Galaxybase  | 使用 galaxybase-console-buildgraph 工具导入 |
|  TigerGraph  |              GSQL 声明加载作业              |
|    Neo4j     |      使用 neo4j-admin import 离线导入       |
|  HugeGraph   |       使用 HugeGraph-Loader 工具导入        |
|    Nebula    |        使用 Nebula Importer 工具导入        |



### 三、查询性能测试

​		数据查询是图数据库的基本功能之一。图数据库应支持用户所需的各种查询，并且追求快速的执行速度。在本次测试中，对于每个测试项，我们将执行三次，第一次用来排除冷启动的干扰，取第二、三次平均耗时的平均值作为结果，同时记录查询失败的比例，超时和报错都将视作查询失败。对于每个参与测试的图数据库，我们将调用已知的最优的方法实现查询。具体的调用方式或调用语句可查看项目中源码部分。

​		查询性能测试主要检测以下两个个方面：

- K-hop neighbor 的查询响应时间
- 常用图算法的查询响应时间

#### 3.1 K-hop neighbor 

​		K-hop neighbor 查询的是起始点 K 度内遍历到的邻居，该测试项将很好地体现图遍历性能。对于每个数据集，我们都准备了300个样本数据（该样本集取自 [TigerGraph 的benchmark项目](https://github.com/tigergraph/graph-database-benchmark) ），测试时按顺序读取，统计其N度扩展所遍历到邻居点的个数，记录平均时间进行对比。分别测试N=1、2、3、4、5、6的情况。在N为1、2时，设置超时时间为3分钟/查询，3度及以上时，设置超时时间为1小时/查询。为了将测试重心集中在图遍历和最小化网络输出时间上，我们只输出 K 度内邻居数量的大小，而不是完整的邻居列表。我们在参与测试的图数据库中，对所有测试查询结果进行交叉验证。另外，由于部分图数据库3-6度查询耗时过长，且未必能算出结果，为了降低测试的时间成本，我们将测试样本数量减少到10个（取300个样本中的前10个）。

#### 3.2 常用图算法

​		本次测试涉及的常用图算法包括：

* Shortest Path
* PageRank 
* Weakly Connected Components 
* Label Propagation Algorithm
* Degree Centrality

其中，Shortest Path 的测试使用了100个样本，路径长度为1-5的样本各占20%。其他算法都是全图算法，不需要设置样本。常用图算法的超时时间设置为1小时。



### 四、项目结构与执行方式

​		为了方便参与测试的各个图数据库完成测试项，我们设计了一个测试框架，主要包括一些抽象类和接口，各个图数据库需要实现这些类和接口，以便识别配置文件，按执行逻辑运行测试项。

#### 4.1 项目结构介绍

**common：** 共同的测试框架，包括了完整的测试逻辑和流程，各个图数据库的测试模块都基于该框架实现。

* **galaxybase-developer：** Galaxybase 开发者版测试模块

* **tigerGraph：** TigerGraph 的测试模块

* **neo4j：** Neo4j 测试模块

* **hugeGraph：** HugeGraph 测试模块

* **nebula：** Nebula 测试模块

#### 4.2 配置文件介绍

​		测试项的自动化执行需要依赖配置文件 `application.yaml` ，该配置文件主要实现以下配置：

* 指定图数据库类型和图名称
* 指定测试样本文件路径
* 设置超时时间
* 设置测试项、测试样本数量、测试项循环次数

每个图数据库模块实现了共同框架之后都应按需要编辑配置文件，以便测试程序正确地连接图数据库，读取测试样本，执行测试项。以下是测试 Galaxybase 图数据库的配置样例：

```yaml
# （必须）图数据库类型
graphType: galaxybase
# （必须）图名称
graphName: Graph500
# （必须）默认样本文件路径（与测试程序所在目录的相对路径），如果测试项中没有指定样本文件，则默认读取本样本文件
sample: sample/Graph500-kn-sample.txt
# （非必须）测试中需要多个样本文件，可在此处指定样本路径到样本名称映射，后续测试项上通过样本名称指定样本。
samples:
  # （可添加多条）样本名称: 样本所在路径
  sp: sample/Graph500-sp-sample.txt
# （必须）默认超时时间，测试项也支持独立指定超时时间，单位：s，超时后将强制关闭当前测试任务
timeout: 3600
# （非必须，默认值为true）测试任务超时后是否停止超时任务所在的测试项，直接开始下一个测试项的测试
timeoutStop: false
# （非必须，默认值为true）遇到错误后是否停止超时任务所在的测试项，直接开始下一个测试项的测试
errorStop: false
# （必须）数据库连接实现类的包名（全限定名）
dataBaseClass: com.galaxybase.benchmark.galaxybase.developer.database.GalaxyData
# （非必须）各个数据库测试实现的自定义参数区，根据不同数据库将有所不同，请参照各个测试模块的README说明文档
parameters:
  # 此处为galaxybas的自定义参数示例
  vertexType: person
  edgeType: know
  ip: localhost
  port: 7687
  user: admin
  password: admin
# (必须)测试项列表（有序）
test:
  # Galaxybase K-hop neighbor 1度查询配置示例
  # （必须）KNeighbor1实现类的包名（全限定名）
  - testClass: com.galaxybase.benchmark.galaxybase.developer.item.KNeighbor1
    # （非必须，默认值为1）当前测试项的测试任务数
    count: 300
    # （非必须，默认值为1）当前测试项的循环测试次数，次数越多测试结果越稳定
    loop: 3
    # （非必须，默认值为删除配置的默认超时时间的值）超时时间，超时后将强制关闭当前测试任务
    timeout: 180
......

  #Shortest Path算法测试
  # （必须）ShortestPath实现类的包名（全限定名）
  - testClass: com.galaxybase.benchmark.galaxybase.developer.item.ShortestPath
    # （非必须，默认值为1）当前测试项的测试任务数
    count: 100
    # （非必须，默认值为1）当前测试项的循环测试次数，次数越多测试结果越稳定
    loop: 3
    # （非必须，默认值为上述`sample`配置的默认样本）指定当前测试项采用的测试样本名称
    sample: sp
```

#### 4.3 运行测试

​		各个图数据库的执行步骤可参考各自对应模块的 README 文档。