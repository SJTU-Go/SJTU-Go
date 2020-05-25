// pages/walk/walk.js

Page({
  data: {
    routeplan:[],
    polyline:[],
    time:0,
    depart:'',
    arrive:'',
    pass:'',
    tmphis:[],
    tmproute:[]
  },

  onLoad: function(options){
    var d = JSON.parse(options.RT)
    this.setData({
      routeplan: JSON.parse(options.RT),
      time:options.travelTime,
    })
    var polyline = [];
    console.log(options.travelTime);
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
      if(item.type=="WALK"){
        line['color']='#0099FF';
      }
      else{line['color']='#00CC33'}
      line['points']=points;
      //line['color']='#808080';
      line['width']=4;
      //line['dottedLine']=true;
      //console.log(line);
      polyline.push(line);
    }
    this.setData({
      polyline:polyline
    })
    var that=this
    wx.getStorage({
      key: 'depart',
      success:function(res){      
        that.setData({depart:res.data.name})
      }
    })   
    wx.getStorage({
      key: 'arrive',
      success:function(res){that.setData({arrive:res.data.name})}
    })
    wx.getStorage({
      key: 'pass',
      success:function(res){that.setData({pass:res.data.name})}
    })
    wx.getStorage({
      key: 'history',
      success:function(res){that.setData({tmphis:res.data})}
    })
    wx.getStorage({
      key: 'historyroute',
      success:function(res){that.setData({tmproute:res.data})}
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
    wx.setStorage({
      data: history,
      key: 'history',
    })
    wx.setStorage({
      data: historyroute,
      key: 'historyroute',
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