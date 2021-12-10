Vue.component("webview", {
	data: function() {
		return {
		}
	},
	template: `<div :class="'iWebView'"><slot></slot></div>`,
	props: {

	},
	mounted() {
		// console.log(this.$parent);
		// console.log(this.$slots);
		this.$nextTick(()=>{
			for (var i= 0;i<this.$slots.default.length;i++){
				if (this.$slots.default[i].elm.outerHTML!= undefined){
					// console.log( this.$slots.default[i].elm.innerHTML)
				}
			}
		})
	},
	methods: {
		
	}

});
