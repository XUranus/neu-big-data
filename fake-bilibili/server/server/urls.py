"""server URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.conf.urls import url
from django.shortcuts import *

import sys
sys.path.append('./server')
import controller as cnt


urlpatterns = [
    url(r'^get_uid$',cnt.get_uid),
    url(r'^video_info$',cnt.video_info),
    url(r'^login$',cnt.login),
    url(r'^watched$',cnt.watched),
    url(r'^recommend$',cnt.loadRecommend),
    path('admin/', admin.site.urls),
]
