Vue.component("isfollow", {
	data: function() {
		return {
			is_follow:false,
			is_show:false,
			qrcode_img:"",
			bg_width:0,
			bg_height:0,
		}
	},
	template: `<div :class="'isFollowMash'" v-show="is_show"><div class='follow_close_button' @click='closeFollowAlert()'><i class="iconfont icon-guanbi"></i></div><div class="isFollowContent" ref="content"><slot></slot><img :src='qrcode_img'></div></div>`,
	props: {

	},
	mounted() {
		this.checkFollow();
	},
	methods: {
		closeFollowAlert(){
			this.is_show = false
			localReadLib.setAdvStation(true); // 暂停广告位
			this.$emit("is_show",this.is_show)
		},
		checkFollow(){
			var local_data = localReadLib.loadUpdateTime("check_follow");
			if (local_data == null){
				console.log("检测是否有本地数据")
			}
			else{
				//console.log(local_data,"有本地数据，不弹窗")
				return
			}
						
			var post_data = {
				"token":getToken()
			}
			axios.post(api_list.getJudge,post_data).then(result=>{
				console.log(JSON.stringify(result.data))
				if (result.data.code === 0){
					if (result.data.is_follow!=="Y"){
						// 跳弹窗
						localReadLib.setAdvStation(false); // 暂停广告位
						this.qrcode_img = result.data.official_account_invite_img
						this.is_follow = false
						this.is_show = true
						this.$emit("is_show",this.is_show)
						var expire_time = 3 * 60 * 60 // 3小时*60分钟*60秒 
						localReadLib.save("check_follow","checked",expire_time)
					}
					else{
						// 啥也不做
					}
				}
				else{}
			}).then(error=>{
				error!==undefined && console.log(JSON.stringify(error))
			})
		},
	}

});
