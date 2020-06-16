// pages/walk/walk.js

Page({
  data: {
    lon:0,
    lat:0,
    routeplan:[],
    polyline:[],
    time:0,
    depart:'',
    arrive:'',
    pass:'',
    tmphis:[],
    tmproute:[],
    tmpplan:[],
    storage:'',
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
        this.setData({lon:i[0],lat:i[1]})
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
    wx.getStorage({
      key: 'plan',
      success:function(res){that.setData({tmpplan:res.data})}
    })
    var p
     var storage={}
    var id
     wx.getStorage({
      key: 'userID',
     success(res){
       id = res.data
    wx.getStorage({
      key: 'walk',
      success(res){p  = res.data
       storage.strategy = p
       storage.userID = id
       that.setData({storage:storage})
         console.log(that.data.storage)
     }
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
      wx.request({
        url: 'https://api.ltzhou.com/trip/start',
        method:"POST",
        data:this.data.storage,
          success (res) {console.log("assessing");console.log(res.data)
          var addid=res.data
        var numberid;
        wx.getStorage({
          key: 'tripID',
          success(res){numberid=res.data;numberid.push(addid)
          wx.setStorage({
            data: numberid,
            key: 'tripID',
          })
          
          }
        })
        
        
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