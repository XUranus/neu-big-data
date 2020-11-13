# -*- coding: utf-8 -*-
import json
import os
import pandas as pd

def readcorr(path):
    return pd.read_csv(path)
 
def init_corrcsv():     #读取本地相似矩阵csv文件
    #testread = readcorr('C:\\Users\\gjc\\Desktop\\corr.csv')
    testread = testread.set_index('aid')
    return testread
 
def eachFile(filepath):     #遍历文件夹，获取所有文件目录
    files = []
    pathDir =  os.listdir(filepath)
    for allDir in pathDir:
        child = os.path.join('%s/%s' % (filepath, allDir))
        if os.path.isfile(child):
            #f = child.decode('gbk') # .decode('gbk')是解决中文显示乱码问题
            files.append(child)
            continue
        eachFile(child)
    return files
 
def init_data(filepath):    #读取本地数据构造dataframe
    pathlist = eachFile(filepath)
    df = pd.DataFrame(columns=['share', 'favorite', 'like', 'danmaku', 'reply', 'dislike', 'coin', 'view'])
    for f in pathlist:
        with open(f) as load_f:
            load_dict = json.load(load_f)
            aid = load_dict['data']['data']['aid']
            share = load_dict['data']['data']['share']
            favorite = load_dict['data']['data']['favorite']
            like = load_dict['data']['data']['like']
            danmaku = load_dict['data']['data']['danmaku']
            reply = load_dict['data']['data']['reply']
            dislike = load_dict['data']['data']['dislike']
            coin = load_dict['data']['data']['coin']
            view = load_dict['data']['data']['view']
            df.loc[aid] = [share, favorite, like, danmaku, reply, dislike, coin, view]
    df = df.apply(lambda x: x.astype(int))
    df = df.reset_index()
    df.rename(columns={'index': 'aid'}, inplace=True)
    df = df.pivot_table(columns='aid',values=['share', 'favorite', 'like', 'danmaku', 'reply', 'dislike', 'coin', 'view'])
    return df
 
def getcorr(datapath):
    df = init_data(datapath)    #获得数据dataframe
    corr = df.corr()    #计算相似矩阵
    #corr.to_csv('C:\\Users\\gjc\\Desktop\\corr.csv') #讲相似矩阵写入本地保存
 
def gettopn(aid,n):  #获取与某个视频最相关的n个视频
    all_corr = pd.DataFrame(df_corr.loc[aid])
    all_corr.columns=['corr']
    sort_aid = all_corr.sort_values(by='corr',ascending=False)[1:n]
    return sort_aid
 
def commend(aidlist):   #根据输入视频列表，获取最相关的几个视频
    n = 50 #每个视频找最相关的n部
    m = 50 #最后返回m部，未去重
    corr1 = gettopn(aidlist[0],n)
    i = 1
    while(i < len(aidlist)):
        corr2 = gettopn(aidlist[i],n)
        corr1 = pd.concat([corr1,corr2],axis=0)
        i = i+1
    corr_aid = corr1.sort_values(by='corr',ascending=False)[:m].index.values.tolist()
    for c in corr_aid:
        for a in aidlist:
            if c==a:
                corr_aid.remove(c)
                break
    new_corr_aid = []
    for c in corr_aid:
        if c not in new_corr_aid:
            new_corr_aid.append(c)
        if(len(new_corr_aid)>30):
            break
    print('recommend:',new_corr_aid)
    return new_corr_aid

def getgood(): #开始推荐最好的n个
    n = 30
    df1 = pd.DataFrame(df.loc['view'])
    print(df1.info())
    df2 = df1.sort_values(by='view',ascending=False)[1:n]
    return df2.index.values.tolist()
    #return df.sort_values(by='like',ascending='False').head(10).index

#init
print('init model start...')
df = init_data('/home/xuranus/Desktop/Python大数据/大作业-推荐系统/data')
df_corr = df.corr()
print(df.head())
print(df_corr)
print('init model finished.')