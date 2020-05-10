//index.js
const app = getApp()

Page({
  data:
  {preference:[],
   banned:[]
  },
  setPreference:function()
    {wx.navigateTo({
      url: '../setPreferencePage/setPreferencePage',
     })},
     onLoad: function (options) {
      var that =this;
      wx.getStorage({
      key: 'preference',
      success: function(res){
      that.setData({
        preference:res.data,
      })
      if(that.data.preference.length==0){
      that.setData({
      status:false
      });
      }else{
      that.setData({
      status:true
      })
      }
      },
      fail: function(res) {
      console.log(res+'aaaaa')
      }
      });
      wx.getStorage({
        key: 'banned',
        success: function(res){
        that.setData({
          banned:res.data,
        })
        if(that.data.banned.length==0){
        that.setData({
        status:false
        });
        }else{
        that.setData({
        status:true
        })
        }
        },
        fail: function(res) {
        console.log(res+'aaaaa')
        }
        });
      },

}
)
