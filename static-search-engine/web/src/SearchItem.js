import React from 'react';
import PropTypes from 'prop-types';
import Card from '@material-ui/core/Card';

class SearchItem extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const data = this.props.data;
        const keyword = this.props.keyword;
        var re =new RegExp(keyword,"g"); //定义正则
        for(var k in data) {
            data[k]=data[k].replace(re, `<span style="color:blue">${keyword}</span>`); //进行替换，并定义高亮的样式
        }

        return (
            <Card style={{margin:10,maxHeight:300}}>
                <div style={{margin:10,wordWrap:"break-word"}}>
                <p>中文名：<span dangerouslySetInnerHTML={{__html:data.chinese_name}}></span></p>
                <p>英文名：<span dangerouslySetInnerHTML={{__html:data.english_name}}></span></p>
                <p>著名校友：<span dangerouslySetInnerHTML={{__html:data.famous_alumni}}></span></p>
                <p>成立时间：<span dangerouslySetInnerHTML={{__html:data.found_time}}></span></p>
                <p>地点：<span dangerouslySetInnerHTML={{__html:data.location}}></span></p>
                <p>主要院系：<span dangerouslySetInnerHTML={{__html:data.major_department}}></span></p>
                </div>
            </Card>
        );
      }
}

SearchItem.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default SearchItem;

