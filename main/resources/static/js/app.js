// =======================
// LOGIN CHECK
// =======================
if (localStorage.getItem("loggedIn") !== "true") {
    window.location.href = "login.html";
}

// =======================
// GLOBALS
// =======================
let allTrades = [];
let chart, lineChart, portfolioChart;
let wallet = 0;

// =======================
// LOGOUT
// =======================
function logout() {
    localStorage.removeItem("loggedIn");
    window.location.href = "login.html";
}

// =======================
// NAVIGATION
// =======================
function goTo(id) {
    document.getElementById(id).scrollIntoView({ behavior: "smooth" });
}

// =======================
// SIDEBAR
// =======================
function toggleMenu() {
    let s = document.getElementById("sidebar");
    s.style.left = (s.style.left === "0px") ? "-200px" : "0px";
}

// AUTO CLOSE SIDEBAR
document.addEventListener("click", function(e) {
    let sidebar = document.getElementById("sidebar");
    let menu = document.querySelector(".menu");

    if (!sidebar || !menu) return;

    if (!sidebar.contains(e.target) && !menu.contains(e.target)) {
        sidebar.style.left = "-200px";
    }
});

// =======================
// THEME
// =======================
function toggleTheme() {
    document.body.classList.toggle("light");
}

// =======================
// BUY / SELL
// =======================
function buy() {
    let coin = document.getElementById("coinSelect").value;
    let amt = parseFloat(document.getElementById("amount").value);

    if (isNaN(amt) || amt <= 0) return alert("Enter valid amount");

    fetch("http://localhost:8087/api/trades", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ type: "BUY", coin, amount: amt })
    }).then(() => loadHistory());
}

function sell() {
    let coin = document.getElementById("coinSelect").value;
    let amt = parseFloat(document.getElementById("amount").value);

    if (isNaN(amt) || amt <= 0) return alert("Enter valid amount");

    fetch("http://localhost:8087/api/trades", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ type: "SELL", coin, amount: amt })
    }).then(() => loadHistory());
}

// =======================
// LOAD HISTORY
// =======================
function loadHistory() {
    fetch("http://localhost:8087/api/trades")
    .then(res => res.json())
    .then(data => {
        allTrades = data;
        renderTrades(data);
        updateSummary();
        updateRecommendation();
        updatePortfolio();
    });
}

// =======================
// RENDER HISTORY
// =======================
function renderTrades(data) {
    let list = document.getElementById("history");
    list.innerHTML = "";

    data.forEach(tx => {
        let li = document.createElement("li");
        li.innerHTML = `${tx.type} ${tx.coin} ₹${tx.amount}`;
        list.appendChild(li);
    });
}

// =======================
// SUMMARY
// =======================
function updateSummary() {
    let buy = 0, sell = 0;

    allTrades.forEach(t => {
        if (t.type === "BUY") buy += t.amount;
        else sell += t.amount;
    });

    document.getElementById("summaryBox").innerHTML =
        `Buy: ₹${buy} | Sell: ₹${sell}`;
}

// =======================
// RECOMMENDATION
// =======================
function updateRecommendation() {
    document.getElementById("recommendBox").innerHTML = "HOLD 🤝";
}

// =======================
// MARKET DATA
// =======================
function loadData() {
    fetch('https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,solana&vs_currencies=inr')
    .then(res => res.json())
    .then(data => {

        let btc = data.bitcoin.inr;
        let eth = data.ethereum.inr;
        let sol = data.solana.inr;

        document.getElementById("btc").innerText = "₹ " + btc;
        document.getElementById("eth").innerText = "₹ " + eth;
        document.getElementById("sol").innerText = "₹ " + sol;

        updateChart(btc, eth, sol);
      updateLineChart(
          btc + Math.random() * 500,
          eth + Math.random() * 50,
          sol + Math.random() * 5
       );
    });
}

// =======================
// BAR CHART
// =======================
function updateChart(btc, eth, sol) {
    let ctx = document.getElementById("chart")?.getContext("2d");
    if (!ctx) return;

    if (chart) chart.destroy();

    chart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["BTC", "ETH", "SOL"],
            datasets: [{
                label: "Price",
                data: [btc, eth, sol]
            }]
        }
    });
}

// =======================
// LINE CHART
// =======================
let historyData = [];

function updateLineChart(btc, eth, sol) {
    historyData.push({ btc, eth, sol });

    if (historyData.length > 20) historyData.shift();

    let ctx = document.getElementById("lineChart")?.getContext("2d");
    if (!ctx) return;

    if (lineChart) lineChart.destroy();

    lineChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: historyData.map((_, i) => "T" + i),
            datasets: [
                {
                    label: "BTC",
                    data: historyData.map(x => x.btc),
                    borderWidth: 2,
                    tension: 0.4
                },
                {
                    label: "ETH",
                    data: historyData.map(x => x.eth),
                    borderWidth: 2,
                    tension: 0.4
                },
                {
                    label: "SOL",
                    data: historyData.map(x => x.sol),
                    borderWidth: 2,
                    tension: 0.4
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    labels: { color: "white" }
                }
            },
          scales: {
              x: {
                  ticks: { color: "white" }
              },
              y: {
                  ticks: { color: "white" },
                  beginAtZero: false
              }
          }
        }
    });
}

// =======================
// PORTFOLIO CHART (FIXED)
// =======================
function updatePortfolio() {
    let h = { BTC: 0, ETH: 0, SOL: 0 };

    allTrades.forEach(t => {
        let coin = t.coin.toUpperCase();

        if (t.type === "BUY") h[coin] += t.amount;
        else h[coin] -= t.amount;
    });

    let values = [h.BTC, h.ETH, h.SOL];

    // 🔥 FIX: avoid invisible pie
    if (values.every(v => v === 0)) {
        values = [10, 10, 10];
    }

    let pieCtx = document.getElementById("portfolioChart")?.getContext("2d");
    if (!pieCtx) return;

    if (portfolioChart) portfolioChart.destroy();

    portfolioChart = new Chart(pieCtx, {
        type: "pie",
        data: {
            labels: ["BTC", "ETH", "SOL"],
            datasets: [{
                data: values
            }]
        }
    });
}

// =======================
// TRADINGVIEW
// =======================
function loadTradingView(symbol = "BINANCE:BTCUSDT") {
    if (!window.TradingView) return;

    document.getElementById("tradingview_chart").innerHTML = "";

    new TradingView.widget({
        container_id: "tradingview_chart",
        width: "100%",
        height: 400,
        symbol: symbol,
        interval: "D",
        theme: "dark"
    });
}

// =======================
// INIT
// =======================
window.onload = () => {
    loadData();
    loadHistory();
    loadTradingView();

    setInterval(loadData, 3000); // 🔥 slower = better graph
};