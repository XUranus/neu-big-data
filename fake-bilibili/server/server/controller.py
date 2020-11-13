from django.contrib import admin
from django.urls import path
from django.conf.urls import url
from django.shortcuts import *
from django.views.decorators import csrf
from django.views.decorators.csrf import csrf_protect
from django.http.response import JsonResponse
import json 
import sys
import MySQLdb as mdb

sys.path.append('./server')
import algorithm as al

#初始化mysql链接
print('init mysql connection...')
import pymysql
pymysql.install_as_MySQLdb()
import MySQLdb
db = MySQLdb.connect(
    host='172.17.11.34',
    user='root',
    passwd='123',
    db='pythonlab',
)
print('init mysql connection finished.')

#登录，记录cookies
def login(request):
    uid = json.loads(request.body)['uid']
    print(uid)
    response = JsonResponse({'succ':True})
    response.set_cookie('uid',uid)
    return response

#获得uid
def get_uid(request):
    uid = request.COOKIES['uid']
    return JsonResponse({'uid':uid})

#看完一个视频，数据库记录播放历史
def watched(request):
    uid = request.COOKIES['uid']
    aid = json.loads(request.body)['aid']
    print(aid)
    #对应的aid watch在数据库键值+1
    cursor = db.cursor()
    sql = "insert ignore into user_aid values ('%s','%s')"%(uid,aid)
    try:
        db.ping(reconnect=True)
        cursor.execute(sql)
        db.commit()
    except:
        db.rollback()
        print("insert play history error")
    return JsonResponse({'succ':True})

#根据uid加载推荐的视频
def loadRecommend(request):
    uid = request.COOKIES['uid']
    print('request recommend list,uid:',uid)
    #返回推荐的videos
    watched_list = history_playlist(uid)
    videos = [] #返回的aid list
    if(len(watched_list)>0): 
        print('watched list:',watched_list)
        print('recommend by personal perference')
        videos = al.commend(watched_list)
    else:
        print('first recommend (by click)')
        videos = al.getgood()
    return JsonResponse({'succ':True,'videos':videos})

#根据aid获取视频信息
def video_info(request):
    return JsonResponse({'succ':False})


    cursor = db.cursor()
    aid = json.loads(request.body)['aid']
    sql = "select from video where aid= %s" % (aid)
    print(sql)
    try:
        db.ping(reconnect=True)
        cursor.execute(sql)
        results = cursor.fetchall()
        if(len(results)==1):
            cursor.close()
            return JsonResponse({'succ':True,'data':results[0]})
    except:
        print("Error: unable to fecth data")
        cursor.close()
        return JsonResponse({'succ':False})
    return JsonResponse({'succ':False})

#获得uid的历史播放记录
def history_playlist(uid):
    cursor = db.cursor()
    sql = "select aid from user_aid WHERE uid = %s"%(uid)
    aid = []
    try:
        db.ping(reconnect=True)
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            avid = row[0]
            aid.append(avid)
    except:
        print("Error: history_playlist() unable to fetch data")
    return aid
 
