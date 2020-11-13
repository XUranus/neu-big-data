import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';

import VideoCard from './VideoCard';
import Slider from 'react-animated-slider';
import 'react-animated-slider/build/horizontal.css';
import axios from 'axios';
import env from './EnvLoader';

const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  paper: {
    padding: theme.spacing.unit,
    textAlign: 'center',
    color: theme.palette.text.secondary,
  },
});



function HomeSectionSlider(props) {
  return (
    <div className="container">
        <Slider autoplay>
          {[
          '/static/banner1.jpeg',
          '/static/banner2.jpeg',
          '/static/banner3.jpeg',
          '/static/banner4.jpeg'
          ].map((x)=>{
            return (<div style={{textAlign:"center"}}>
            <img style={{textAlign:'cener'}} 
              style={{width:'100%',height:'400px'}}
              src={x} ></img>
            </div>)
          })}
        </Slider>
      </div>
  );
}

function Videos(props) {
  const videos = props.videos;

  return (
    <div style={{margin:'10px'}}>
      <br/>
      <Grid container spacing={24}>
        {videos.map((id)=>{
          return (
            <Grid item xs={2}>
              <Paper>
                  <VideoCard aid={id}/>
              </Paper>
            </Grid>
          )
        })}
    </Grid>
    </div>
  );
}


class HomeSection extends React.Component {
    state = {
        videos:[]
    }

    render() {
        const { classes } = this.props;
        const videos = this.state.videos;
        return (
            <div className={classes.root}>
                <HomeSectionSlider/>
                <Grid container spacing={8}>
                    {(videos.length==0)?(<h2>推荐加载中...</h2>):<Videos videos={videos}/>}
                </Grid>
                <hr></hr>
                <p style={{textAlign:'center'}}>你已到达世界的尽头</p>
            </div>
        );
    }

    loadRecommendVideo() {
      var _this = this;
      axios({
          method:'post',
          url:env.apiServerAddr+'/recommend',
          data:{},
          withCredentials:true,
          crossDomain:true,
      }).then(function (res) {
          console.log(res);
          if(res.data.succ) {
            _this.setState(res.data)
          }
      }).catch(function (error) {
          console.log(error);
      });
    }

    componentDidMount() {
      //document.body.style.backgroundImage="url(/static/main-bg.jpg)";
      //document.body.style.height="100%";
      //document.body.style.width="100%";
      //document.body.style.backgroundSize="100%";
      this.loadRecommendVideo();
    }
}


export default withStyles(styles)(HomeSection);