//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            cards:[],
            navbar:true,
            title:true,
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        loadData: function () {
            axios.get("http://localhost:8080/api/clients/1")
                .then(response => {
                    this.data = response.data;
                    this.cards = this.data.cards.sort((a, b) => a.id - b.id);
                    this.data.accounts.sort((a, b) => a.id - b.id);
                })
                .catch(err => console.log(err));
        },
        updateScreenSize:function() {
            this.title = window.innerWidth < 500;
            this.navbar = window.innerWidth < 750;
        },
        changeBackground:function(events){
            if(events.target.checked){
            document.body.classList.remove('image_day');
            document.body.classList.toggle('image_night');
            }
            else{
            document.body.classList.remove('image_night');
            document.body.classList.toggle('image_day');
            }
        },
        colorCard:function(color){
            if(color=="SILVER")
            return "var(--color-silver)";
            if(color=="GOLD")
            return "var(--color-golden)";
            else
            return "var(--color-titanium)";
        },
    },
    mounted(){
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
        beforeDestroy() {
        window.removeEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');