//index.js
const app = getApp()

Page({
  data:
  {preference:[],
   method:['步行','校园巴士','共享单车',"旋风E100"],
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
 /*       if(that.data.banned.length==0){
        that.setData({
        status:false
        });
        }else{
        that.setData({
        status:true
        })
        }*/
        },
        fail: function(res) {
        console.log(res+'aaaaa')
        }
        });
        console.log(that.data.preference.length==0&&that.data.banned.length==0)
console.log("test")
console.log(that.data)
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
