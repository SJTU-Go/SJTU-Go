//userInfoPage.js
const app = getApp()

Page({
  data:{
    List:[],
    admin:666666,
    num:0
    
  },
  

  onShow:function(){
    var that=this
   
    wx.getStorage({
      key: 'history',
      success:function(res){
        console.log(res.data)
        that.setData({List:res.data})
        var l=0
        for(var i in res.data.hia){l+=1}
        that.setData({num:l})
      }
    })
    console.log(this.data)
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
      List: that.data.List
    })
   
    wx.setStorage({
      data:{hia: that.data.List.hia,},
        
      key: 'history',
    })  
    //var route = 'routeList['+index+']'
    console.log(that.data.List)
 
    



},
submitInfo:function(){
  var that=this
  console.log(that.data.List)
  for (var i in that.data.List.hia){
  wx.request({
    url: 'https://api.ltzhou.com/modification/modify/park',
    method:'POST',
    header: {
    'content-type': 'application/json'
    },
    data:{
    "adminID": that.data.admin,
    "message": that.data.List.hia[i].newInfo,
    "placeID": that.data.List.hia[i].id,
    },

    success (res){console.log(res)}
     
  })
}
wx.setStorage({
  data:[],
    
  key: 'history',
}) 
that.setData({
  List: []
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
notice:function(){
  wx.navigateTo({url: '../noticePage/noticePage', })
},
})
