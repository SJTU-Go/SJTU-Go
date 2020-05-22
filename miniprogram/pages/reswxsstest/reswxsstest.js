//index.js
const app = getApp()
Page({
  data:{
    currentData : 0,
    index:['walk','bus'],
    value : new Array(),
    method:["步行","校园巴士"],
    preference:["步行","校园巴士"],
    preferencelist:[{type:"步行",travelTime:2240,distance:898},
  
    {type:"校园巴士",travelTime:1587,distance:8981}
  ],
    routeplan:new Array(),
  },
  onLoad:function(options){   
  },
  searchPagebus: function()
  {
    wx.navigateTo({
      url: '../search/search?RT='+JSON.stringify(this.data.routeplan),})
    
  },
  checkCurrent:function(e){
    const that = this;
 
    if (that.data.currentData === e.target.dataset.current){
        return false;
    }else{
 
      that.setData({
        currentData: e.target.dataset.current
      })
    }},
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})

