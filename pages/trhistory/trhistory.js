// pages/trhistory/trhistory.js
function json2Form(json) { 
  var str = []; 
  for(var p in json){ 
    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(json[p])); 
  } 
  return str.join("&"); 
} 
Page({

  /**
   * 页面的初始数据
   */
  data: {
    admin:'',
    hislist:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this
    wx.getStorage({
      key: 'admin',
      success:function(res){
        console.log(res.data)
        that.setData({admin:res.data})
        
      }
    })
    wx.request({
      url: 'https://api.ltzhou.com/modification/view',
      method:'POST',
      header: {
      'content-type': 'application/x-www-form-urlencoded'
      },
     data:json2Form({
       adminID:1
     }),
  
     success (res){
      console.log(res)
      var mm= new Object();
      var count=0;
      for (var i in res.data){
        if  (res.data[i].adminID==1 && res.data[i].contents[0]=='T'){
          var l=new Object()
          var r=res.data[i].contents
          console.log(typeof(r))
          var len=r.length
          console.log(len)
          var str=res.data[i].contents.substring(1,len-2)
          console.log(str)
          var st=str.indexOf('relatedVertex')
          console.log(st)
          var en=str.indexOf('repeatTime')
          console.log(en)
          var verlist=str.substring(st,en)
          var repeat=str.substring(en)
          console.log(repeat)
          var other=str.substring(11,st-2)
          console.log(other)
          var lis=other.split(', ')
          console.log(lis)
          for (var ii=0;ii<lis.length;++ii){
            console.log(lis[ii].split('='))
            var key=lis[ii].split('=')[0]
            var value=lis[ii].split('=')[1]
            l[key]=value
          }
          l.repeatTime=repeat
          mm[count]=res.data[i]
          mm[count].detail=l
          count+=1
        }
      }
      console.log(mm)
      that.setData({hislist :mm})
      
    }
       
    })
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})