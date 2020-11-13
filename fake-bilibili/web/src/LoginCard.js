import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';


import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import axios from 'axios';
import env from './EnvLoader';
import { Paper, Hidden } from '@material-ui/core';

const styles = {
  card: {
    maxWidth: 350,
    width: 'auto',
    display: 'flex', // Fix IE 11 issue
  },
  media: {
    height: 140,
  },
};

class LoginCard extends React.Component {

  handleSignin() {
    var uid = document.getElementById('uid').value;
    axios({
      method:'post',
      url:env.apiServerAddr+'/login',
      data:{uid:uid},
      withCredentials:true,
      crossDomain:true,
    }).then(function (res) {
      console.log(res);
      if(res.data.succ) {
        window.location.href = "/"
      }
    }).catch(function (error) {
      console.log(error);
    });
  }

  render() {
    const { classes } = this.props;
    return (
      <Paper style={{textAlign:'center',margin:'100px 200px 100px 200px',maxWidth:'400px'}}>
        <Card >
      <CardActionArea>
        <CardMedia
          className={classes.media}
          image="/static/2233.jpeg"
          title="Contemplative Reptile"
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="h2">
            用户登录
          </Typography>
          <div className={classes.form}>
            <FormControl margin="normal" required fullWidth>
              <InputLabel htmlFor="user_mail">UID</InputLabel>
              <Input id="uid" name="user_mail" autoComplete="email" autoFocus />
            </FormControl>
            <FormControl margin="normal" required fullWidth>
              <InputLabel htmlFor="user_pass">密码</InputLabel>
              <Input name="user_pass" type="password" id="user_pass" autoComplete="current-password" />
            </FormControl>
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="记住密码"
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
              onClick={this.handleSignin}
            >
              登陆
            </Button>
          </div>
        </CardContent>
      </CardActionArea>
    </Card>
      </Paper>
    );
  }

  componentDidMount() {
    document.body.style.backgroundImage="url(/static/login-bg.jpg)";
    document.body.style.height="100%";
    document.body.style.width="100%";
    document.body.style.overflow=Hidden;
    document.body.style.backgroundSize="100%";
  }
}

LoginCard.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(LoginCard);