//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            cards:[],
            navbar:true,
            title:true,
            typeCard:"CREDIT",
            allCards:[],
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        loadData: function () {
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data;
                    this.data.accounts.sort((a, b) => a.id - b.id);
                    this.allCards=this.data.cards;
                    this.cardType()
                })
                .catch(err => console.log(err));
        },
        logout:function() {
            axios.post('/api/logout') 
                .then(response => {
                    window.location.href = '/web/index.html';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
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
        cardType:function (){
            this.cards = this.allCards.filter(cards => cards.type.includes(this.typeCard));
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