// pages/search/search.js

Page({
  data: {
    routeplan:[],
    polyline:[],
  },

  onLoad: function(options){
    var d = JSON.parse(options.RT)
    this.setData({
      routeplan: JSON.parse(options.RT)
    })
    var polyline = [];
    for(var j=0;j<d.length;j++){
      var line = {};
      var points = [];
      var item = d[j];
      for(var i of item.routePath.coordinates){
        var cor = {};
        cor['longitude']=i[0];
        cor['latitude']=i[1];
        points.push(cor);
      }
      line['points']=points;
      line['color']='#808080';
      line['width']=2;
      line['dottedLine']=true;
      //console.log(line);
      polyline.push(line);
    }
    //console.log(polyline)
    this.setData({
      polyline:polyline
    })
    //console.log(polyline)
  },

  // getdata:function(){
  //   var data=this.data;
  //   console.log(data)
  // },

  switchChange:function(e){
    var that = this;
    var flag = e.detail.value;
    var idx = e.currentTarget.dataset.index;
    var color = 'polyline['+idx+'].color';
    var width = 'polyline['+idx+'].width';
    var dot = 'polyline['+idx+'].dottedLine';
    if(flag==true){
      that.setData({
        [color] : "#0099FF",
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
  },


  // isOpen: function(e){
  //   var that = this;
  //   var idx=e.currentTarget.dataset.index;
  //   var route=that.data.route;
  //   var hiddenn=route[idx].hiddena;
  //   if(hiddenn==true){
  //     that.setData({
  //       ['route[' + idx + '].hiddena'] : !hiddenn,
  //       // ['route[' + idx + '].']
  //     })
  //   }
  // },

  
  onReady: function () {
  },

  onShow: function () {
  },

  onHide: function () {
  },

  onUnload: function () {
  },

  onPullDownRefresh: function () {
  },

  onReachBottom: function () {
  },

  onShareAppMessage: function () {
  }
})