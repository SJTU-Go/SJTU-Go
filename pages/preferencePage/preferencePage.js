//index.js
const app = getApp()

Page({
  data:
  {preference1:['步行','共享单车','校园巴士'],
   preference:[],
   method:['步行','校园巴士','共享单车'],
   banned:[]
  },

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
        console.log(that.data.preference.length==0&&that.data.banned.length==0)


      },
      setPreference:function()
      {wx.navigateTo({
        url: '../setPreferencePage/setPreferencePage',
       })},
       back:function(){wx.switchTab({
         url: '../index/index',
       })}
}
)
