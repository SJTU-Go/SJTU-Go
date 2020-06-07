// pages/bike/bike.js

Page({
  data: {
    routeplan:[],
    polyline:[],
    time:0,
    depart:'',
    arrive:'',
    pass:'',
    tmphis:[],
    tmproute:[],
    tmpplan:[]
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
d=res.data
var polyline = []
for(var j=0;j<d.length;j++){

  var line = {};
  var points = [];
  var item = d[j];
  for(var i of item.pointList.coordinates){
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

  starttirp:function(e){
    var that=this
    var record={};
    record['depart']=that.data.depart
    record['arrive']=that.data.arrive
    record['passPlaces']=that.data.pass
    record['routetime']=this.data.time
    var history=that.data.tmphis
    history.push(record)
    var historyroute=that.data.tmproute
    historyroute.push(that.data.polyline)
    var plan=that.data.tmpplan
    plan.push(that.data.routeplan)
    wx.setStorage({
      data: history,
      key: 'history',
    })
    wx.setStorage({
      data: historyroute,
      key: 'historyroute',
    })
    wx.setStorage({
      data: plan,
      key: 'plan',
    })
    wx.getStorage({
      key: 'history',
      success: function(res) {
        console.log(res.data)
      }
    })
    wx.getStorage({
      key: 'historyroute',
      success: function(res) {
        console.log(res.data)
      }
    })
    wx.getStorage({
      key: 'plan',
      success: function(res) {
        console.log(res.data)
      }
    })
    wx.showToast({
       title: '行程已被记录，请跳转到对应app导航',
       icon: 'none',
       duration: 3000,
       success: function () {
       setTimeout(function () {
       wx.switchTab({
       url: '../index/index',
       })
       }, 2000);
       }
      })
  },
  
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