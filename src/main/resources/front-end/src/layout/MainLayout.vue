<script setup>
import { RouterView } from 'vue-router'
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'
</script>

<template>
  <div class="app-wrapper">
    <!-- Sidebar -->
    <aside class="app-sidebar glass-dark">
      <Sidebar />
    </aside>

    <!-- Main Content -->
    <div class="app-main-content">
      <header class="app-header glass">
        <Header />
      </header>

      <main class="page-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<style scoped>
.app-wrapper {
  display: flex;
  min-height: 100vh;
  background-color: #f1f5f9;
  background-image: 
    radial-gradient(at 0% 0%, rgba(99, 102, 241, 0.15) 0px, transparent 50%),
    radial-gradient(at 100% 0%, rgba(236, 72, 153, 0.15) 0px, transparent 50%);
}

.app-sidebar {
  width: 260px;
  height: 96vh;
  position: fixed;
  left: 12px;
  top: 2vh;
  border-radius: 24px;
  background: white;
  box-shadow: var(--shadow-lg);
  z-index: 100;
  overflow: hidden;
}

.app-main-content {
  flex: 1;
  margin-left: 284px; /* 260px width + 12px left + 12px gap */
  margin-right: 12px;
  padding-top: 2vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  height: 64px;
  border-radius: 16px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: var(--shadow-sm);
  position: sticky;
  top: 20px; 
  z-index: 90;
}

.page-content {
  flex: 1;
  border-radius: 20px;
  padding-bottom: 20px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>
