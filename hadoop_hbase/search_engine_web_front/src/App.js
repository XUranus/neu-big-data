import React, { Component } from 'react';
import SearchBar from './SearchBar'
import Divider from '@material-ui/core/Divider';
import axios from 'axios'
import SearchItem from './SearchItem'
import { element } from 'prop-types';

class App extends Component {
  
  constructor(props) {
    super(props);
    this.state = {
      data:null,
      keyword:null,
    }
  }

  render() {
    const data = this.state.data;

    return (
      <div>
        <SearchBar handleSearch={this.handleSearch.bind(this)}/>
        <Divider/>
        {data===null?(<p>没有数据！</p>):
        data.map(value=>(
          <div>
          <SearchItem data={value} keyword={this.state.keyword}/>
          <br/>
          </div>
        ))}
      </div>
    );
  }

  handleSearch = ()=>{
    var input = document.getElementById('searchword').value;
    //alert(input);

    axios({
      method: 'post',
      url: 'http://172.17.11.40:8081/api/search',
      data: {
        input:input
      }
    }).then((res)=>{ //success
        var data = res.data;
        console.log(data)
        if(data.data) 
          this.setState({
            data:data.data,
            keyword:input,
          });
        else alert('没有数据！');
    }).catch((err)=>{ //error
      console.log(err);
      alert(err);
    });

  }
  

}

export default App;
