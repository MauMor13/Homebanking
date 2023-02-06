const { createApp } = Vue;

createApp({
    data() {
        return {
            data:{},
            state:"accounts",
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData:function(){
            axios.get("http://localhost:8080/api/clients")
            .then(response=>{
                console.log(response.data[0]);
                this.data=response.data[0];
            })
            .catch(err=>console.log(err));
            },
    }
}).mount('#app');