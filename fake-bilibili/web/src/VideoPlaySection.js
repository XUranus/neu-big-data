import React from 'react';
import env from './EnvLoader';
import axios from 'axios'
import Card from '@material-ui/core/Card';

function VideoStatisticCard(props) {
    return (
        <Card>
            <div style={{margin:'10px'}}>
                <p>播放：{props.videoInfo.view}&nbsp;&nbsp;
                分享{props.videoInfo.share}&nbsp;&nbsp;
                赞：{props.videoInfo.like}&nbsp;&nbsp;
                硬币：{props.videoInfo.coin}&nbsp;&nbsp;
                回复：{props.videoInfo.reply}</p>
            </div>
        </Card>
    )    
}

class VideoPlaySection extends React.Component {

    state = {
        videoInfo:{
            aid: 53376,
            share: 5443,
            favorite: 32172,
            like: 1963,
            danmaku: 42370,
            reply: 2943,
            dislike: 0,
            coin: 3547,
            view: 1768651
        }
    }

    loadVideoInfo() {
        axios({
            method:'post',
            url:env.apiServerAddr+'/video_info',
            data:{aid:this.props.location.pathname.split('/')[2]},
            withCredentials:true,
            crossDomain:true,
        }).then(function (res) {
            console.log(res);
        }).catch(function (error) {
            console.log(error);
        });
    }

    informWatch() {
        axios({
            method:'post',
            url:env.apiServerAddr+'/watched',
            data:{aid:this.props.location.pathname.split('/')[2]},
            withCredentials:true,
            crossDomain:true,
        }).then(function (res) {
            console.log(res);
            if(res.data.succ) 
                this.setState({videoInfo:res.data.data})
        }).catch(function (error) {
            console.log(error);
        });
    }

    componentDidMount() {
        document.body.style.backgroundImage="url(/static/background2.png)";
        document.body.style.height="100%";
        document.body.style.width="100%";
        document.body.style.backgroundSize="100%";
        this.informWatch();
        this.loadVideoInfo();
    }

    render() {
        const aid = this.props.location.pathname.split('/')[2]
        return (
            <div style={{textAlign:'center'}}>
                <iframe
                    style={{width:'1200px',height:'800px'}} 
                    src={"//player.bilibili.com/player.html?aid="+aid} scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"> 
                </iframe>
                {this.state.videoInfo==null?
                <h2>加载中....</h2>:
                <VideoStatisticCard videoInfo={this.state.videoInfo}/>
               }
            </div>
        )
    }
}

export default VideoPlaySection;