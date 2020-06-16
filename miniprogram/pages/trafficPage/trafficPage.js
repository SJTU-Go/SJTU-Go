// pages/bike/bike.js

Page({
  data: {
    lon:0,
    lat:0,
    routeplan:[],
    polyline:[],
    trafficInfo:[],
  },

  onLoad: function(options){
    var that =this
    var d=new Array(0)
wx.request({
  url: 'https://api.ltzhou.com/traffic/current',
  success(res)
  {wx.setStorage({
    data:res.data,
    key: 'traffic',
  }
)
that.setData({"trafficInfo":res.data})

d=res.data
var polyline = []
for(var j=0;j<d.length;j++){

  var line = {};
  var points = [];
  var item = d[j];
  for(var i of item.pointList.coordinates){
    that.setData({lat:i[1],lon:i[0]})
    var cor = {};
    cor['longitude']=i[0];
    cor['latitude']=i[1];
    points.push(cor);
  }
  line['color']='#ff0000'
  line['points']=points;
  //line['color']='#808080';
  line['width']=4;
  //line['dottedLine']=true;
  //console.log(line);
  polyline.push(line);
}  

    console.log(polyline)
    that.setData({
      polyline:polyline
    })

}
})
},

switchChange:function(e){
  var that = this;
  var flag = e.detail.value;
  var idx = e.currentTarget.dataset.index;
  var color = 'polyline['+idx+'].color';
  var width = 'polyline['+idx+'].width';
  var dot = 'polyline['+idx+'].dottedLine';
  if(flag==true){
    that.setData({
      [color] : "#ff0000",
      [width] : 4,
      [dot]: false
    })
  }
  else{
    that.setData({
      [color] : "#808080",
      [width] : 2,
      [dot]: true
    })
  }
console.log(that.data.polyline)
},

})