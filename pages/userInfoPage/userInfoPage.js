//userInfoPage.js
const app = getApp()

Page({
  data:{
    adminID:1,
    tList:{},
    List:{},
    admin:'',
    num:0
    
  },
  

  onShow:function(){
    var that=this
    wx.getStorage({
      key: 'admin',
      success:function(res){
        console.log(res.data)
        that.setData({admin:res.data})
        
      }
    })
    wx.getStorage({
      key: 'history',
      success:function(res){
        console.log(res.data)
        var ll=res.data
        that.setData({List:ll})
        var l=0
        for(var i in res.data.hia){l+=1}
        that.setData({num:l})
        console.log(that.data)
      }
      
    })
    console.log(that.data)
    console.log(1+1)
    wx.getStorage({
      key: 'traffichistory',
      success:function(res1){
        console.log(res1.data)
        var ll=res1.data
        that.setData({tList:ll})
        var l=0
        for(var i in res1.data){l+=1}
        that.setData({num:l+that.data.num})
        console.log(that.data)
      }
      
    })
   },
   deletePage:function(e){
     var that= this
    console.log(e)
    var index=e.currentTarget.dataset.index
    console.log(that.data.List)
    const length = that.data.List.hia.length
     
    for (var i = index;i<length-1;i++)
    {that.data.List.hia[i] = that.data.List.hia[i+1]
    }
    that.data.List.hia.pop()
    that.setData({
      List: that.data.List,
      num: that.data.num-1
    })
   
    wx.setStorage({
      data:{hia: that.data.List.hia,},
        
      key: 'history',
    })  
    //var route = 'routeList['+index+']'
    console.log(that.data.List)
 
    



},
deletePage2:function(e){
  var that= this
 console.log(e)
 var index=e.currentTarget.dataset.index
 console.log(that.data.tList)
 const length = that.data.tList.length
  
 for (var i = index;i<length-1;i++)
 {that.data.tList[i] = that.data.tList[i+1]
 }
 that.data.tList.pop()
 that.setData({
   tList: that.data.tList,
   num: that.data.num-1
 })

 wx.setStorage({
   data: that.data.tList,
     
   key: 'traffichistory',
 })  
 //var route = 'routeList['+index+']'
 console.log(that.data.tList)

 



},
submitInfoo:function(){
  var that=this
  wx.getStorage({
    key: 'history',
    success:function(res){
      console.log(res.data)
      var ll=res.data
      that.setData({List:ll})
      
    
  
  for (var i in that.data.List.hia){
    console.log(i)
  wx.request({

    url: 'https://api.ltzhou.com/modification/modify/parking',
    method:'POST',
    header: {
    'content-type': 'application/json'
    },
    data:{
    "adminID": 1,
    "message": that.data.List.hia[i].newInfo,
    "placeID": that.data.List.hia[i].id,
    },

    success (res){console.log(res)}
     
  })
}
}
    
})
wx.setStorage({
  data:[],
    
  key: 'history',
}) 
that.setData({
  List: []
})
wx.getStorage({
  key: 'traffichistory',
  success:function(res){
    console.log(res.data)
    var ll=res.data
    that.setData({tList:ll})
    
  

for (var i in that.data.tList){
  console.log(i)
  var s='https://api.ltzhou.com/modification/modify/traffic?adminID='.concat(that.data.adminID)
wx.request({

  url: s,
  method:'POST',
  header: {
  'content-type': 'application/json'
  },
  data:{
    "adminID": that.data.tList[i].adminID,
    "beginDay":  that.data.tList[i].beginDay,
    "beginTime": that.data.tList[i].beginTime,
    "bikeSpeed": that.data.tList[i].bikeSpeed,
    "carSpeed": that.data.tList[i].carSpeed,
    "endTime": that.data.tList[i].endTime,
    "message": that.data.tList[i].message,
    "motorSpeed": that.data.tList[i].motorSpeed,
    "name": that.data.tList[i].name,
    
    "relatedVertex": that.data.tList[i].relatedVertex,
    "repeatTime": 0
  },

  success (res){console.log(res)}
   
})
}
}
  
})
wx.setStorage({
data:[],
  
key: 'traffichistory',
}) 
that.setData({
tList: []
})
wx.showToast({
   title: '修改成功',
   icon: 'success',
   duration: 2000,
   success: function () {
   setTimeout(function () {
   wx.reLaunch({
   url: '../userInfoPage/userInfoPage',
   })
   }, 2000);
   }
  })
},
history: function () {
  
  wx.navigateTo({url: '../history/history', })
},
hisNotice: function () {
  
  wx.navigateTo({url: '../hisNotice/hisNotice', })
},
notice:function(){
  wx.navigateTo({url: '../noticePage/noticePage', })
},
})
