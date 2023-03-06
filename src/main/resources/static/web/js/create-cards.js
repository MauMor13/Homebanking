const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            navbar: true,
            title: true,
            cardType: "",
            cardColor:"",
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
                    this.allCards = this.data.cards;
                    this.cardType()
                })
                .catch(err => console.log(err));
        },
        logout: function () {
            axios.post('/api/logout')
                .then(response => {
                    window.location.href = '/web/index.html';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
        },
        updateScreenSize: function () {
            this.title = window.innerWidth < 500;
            this.navbar = window.innerWidth < 750;
        },
        beforeDestroy() {
            window.removeEventListener("resize", this.updateScreenSize);
        },
        newCard: function () {
            axios.post('/api/clients/current/cards',`type=${this.cardType}&color=${this.cardColor}`)
                .then(response => {
                    window.location.href = '/web/cards.html';
                })
                .catch(error => {
                    this.error = error.response.data
                    console.log(this.error)
                });
        },
    },
    mounted(){
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');