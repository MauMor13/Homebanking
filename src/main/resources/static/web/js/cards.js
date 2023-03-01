//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            cards: [],
            navbar: true,
            title: true,
            typeCard: "CREDIT",
            allCards: [],
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
        changeBackground: function (events) {
            if (events.target.checked) {
                document.body.classList.remove('image_day');
                document.body.classList.toggle('image_night');
            }
            else {
                document.body.classList.remove('image_night');
                document.body.classList.toggle('image_day');
            }
        },
        colorCard: function (color) {
            if (color == "SILVER")
                return "url(https://media.istockphoto.com/id/1051466618/es/vector/fondo-de-tecnolog%C3%ADa-con-textura-metal.jpg?s=612x612&w=0&k=20&c=TNQ6UyN2SJH8jj2BkQfTzUp0Kxh0GBiAHz1lAevhpVA=)";
            if (color == "GOLD")
                return "url(https://i.pinimg.com/originals/96/36/3c/96363c9337b2d1aad24323b1d9efda72.jpg)";
            else
                return "url(https://media.istockphoto.com/id/1320912181/vector/abstract-gray-gradient-metallic-texture.jpg?s=612x612&w=0&k=20&c=R04SBrq_5Li51HaGl3MCeObiLml0yRSeQfIiweRxmHQ=)";
        },
        cardType: function () {
            this.cards = this.allCards.filter(cards => cards.type.includes(this.typeCard));
        },
    },
    mounted() {
        window.addEventListener("resize", this.updateScreenSize);
        const checkbox = document.querySelector('.my-form input[type="checkbox"]');
        const btns = document.querySelectorAll(".my-form button");
        checkbox.addEventListener("change", function () {
            const checked = this.checked;
            for (const btn of btns) {
                checked ? (btn.disabled = false) : (btn.disabled = true);
            }
        });
    },
}).mount('#app');