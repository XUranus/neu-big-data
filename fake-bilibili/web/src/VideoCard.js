import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import axios from 'axios'
import env from './EnvLoader';

const styles = {
  card: {
    maxWidth: 445,
  },
  media: {
    objectFit: 'cover',
  },
};

class VideoCard extends React.Component {
  state = {
    view:0
  }

  componentDidMount() {
    
  }

  render() {
    const { classes } = this.props;
    return (
      <a href={"/av/"+this.props.aid}>
      <Card className={classes.card}>
        <CardActionArea>
          <CardMedia
            component="img"
            alt="Contemplative Reptile"
            className={classes.media}
            height="140"
            image={"/static/post/"+this.props.aid+".png"}
            title="Contemplative Reptile"
          />
          <CardContent>
            <Typography component="p">
            <span>av：{this.props.aid}</span>
            </Typography>
          </CardContent>
        </CardActionArea>
      </Card>
      </a>
    );
  }
}

VideoCard.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(VideoCard);